package site.minnan.rental.application.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.provider.RoomProviderService;
import site.minnan.rental.application.provider.TenantProviderService;
import site.minnan.rental.application.provider.UtilityProviderService;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.BillTenantEntity;
import site.minnan.rental.domain.entity.BillTenantRelevance;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.mapper.BillTenantRelevanceMapper;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.utils.RedisUtil;
import site.minnan.rental.userinterface.dto.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Reference(check = false)
    private RoomProviderService roomProviderService;

    @Reference(check = false)
    private TenantProviderService tenantProviderService;

    @Reference(check = false)
    private UtilityProviderService utilityProviderService;

    @Autowired
    private BillTenantRelevanceMapper billTenantRelevanceMapper;

    /**
     * 结算账单
     *
     * @param dto
     */
    @Override
    public void settleBill(SettleBillDTO dto) {
        List<Bill> billList = billMapper.selectBatchIds(dto.getBillIdList());
        BigDecimal waterPrice = (BigDecimal) redisUtil.getValue("water_price");
        BigDecimal electricPrice = (BigDecimal) redisUtil.getValue("electricity_price");
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        billList.forEach(bill -> {
            bill.settle(waterPrice, electricPrice);
            bill.setUpdateUser(jwtUser);
        });
        billMapper.settleBatch(billList);
    }

    /**
     * 设置水电单价
     *
     * @param dto
     */
    @Override
    public void setUtilityPrice(SetUtilityPriceDTO dto) {
        Optional.ofNullable(dto.getWaterPrice()).ifPresent(s -> redisUtil.valueSet("water_price", s));
        Optional.ofNullable(dto.getElectricityPrice()).ifPresent(s -> redisUtil.valueSet("electricity_price", s));
        Optional.ofNullable(dto.getAccessCardPrice()).ifPresent(s -> redisUtil.valueSet("access_card_price", s));

    }

    /**
     * 获取水电单价
     *
     * @return
     */
    @Override
    public UtilityPrice getUtilityPrice() {
        BigDecimal waterPrice = (BigDecimal) redisUtil.getValue("water_price");
        BigDecimal electricPrice = (BigDecimal) redisUtil.getValue("electricity_price");
        Integer accessCardPrice = (int)redisUtil.getValue("access_card_price");
        return new UtilityPrice(waterPrice, electricPrice, accessCardPrice);
    }

    /**
     * 将到期账单设置为等待登记水电
     */
    @Override
    public void setBillUnpaid() {
        //将到期账单状态设置为等待登记水电
        DateTime now = DateTime.now();
        Timestamp currentTime = new Timestamp(now.getTime());
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BillStatus.INIT);
        List<Bill> initBillList = billMapper.selectList(queryWrapper);
        List<Bill> initializedBillList = initBillList.stream()
                .filter(e -> DateUtil.isSameDay(e.getCompletedDate(), now))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(initializedBillList)) {
            List<SettleQueryDTO> queryList = initBillList.stream().map(e -> new SettleQueryDTO(e.getRoomId(),
                    e.getUtilityStartId())).collect(Collectors.toList());
            //获取水电情况
            Map<Integer, SettleQueryVO> utilityMap = utilityProviderService.getUtility(queryList);
            UtilityPrice price = getUtilityPrice();
            //结算水电
            for (Bill bill : initializedBillList) {
                SettleQueryVO vo = utilityMap.get(bill.getRoomId());
                JSONObject start = vo.getUtilityStart();
                JSONObject end = vo.getUtilityEnd();
                bill.settleWater(start.getBigDecimal("water"), end.getBigDecimal("water"), price.getWaterPrice());
                bill.settleElectricity(start.getBigDecimal("electricity"), end.getBigDecimal("electricity"),
                        price.getElectricityPrice());
                bill.setUtilityEndId(end.getInt("id"));
                bill.setUpdateUser(JwtUser.builder().id(0).realName("系统").build());
                bill.unsettled();
            }
            //更新
            billMapper.settleBatch(initializedBillList);

            //生成新的账单
            List<Integer> roomIdList = initializedBillList.stream().map(Bill::getRoomId).collect(Collectors.toList());
            JSONArray roomInfoList = roomProviderService.getRoomInfoBatch(roomIdList);
            DateTime oneMonthLater = now.offsetNew(DateField.MONTH, 1);
            List<Bill> newBillList = new ArrayList<>();
            for (int i = 0; i < roomInfoList.size(); i++) {
                JSONObject roomInfo = roomInfoList.getJSONObject(i);
                if (Objects.equals(roomInfo.getStr("status"), RoomStatus.ON_RENT.getValue())) {
                    Bill newBill = Bill.builder()
                            .year(now.year())
                            .month(now.month())
                            .houseId(roomInfo.getInt("houseId"))
                            .houseName(roomInfo.getStr("houseName"))
                            .roomId(roomInfo.getInt("id"))
                            .roomNumber(roomInfo.getStr("roomNumber"))
                            .rent(roomInfo.getInt("price"))
                            .completedDate(oneMonthLater)
                            .utilityStartId(utilityMap.get(roomInfo.getInt("id")).getUtilityEnd().getInt("id"))
                            .status(BillStatus.INIT)
                            .build();
                    newBill.setCreateUser(0, "系统", currentTime);
                    newBillList.add(newBill);
                }
            }
            billMapper.insertBatch(newBillList);
            Map<Integer, List<Integer>> tenantIdMap = tenantProviderService.getTenantIdByRoomId(roomIdList);
            List<BillTenantRelevance> relevanceList = newBillList.stream()
                    .flatMap(e -> tenantIdMap.get(e.getRoomId()).stream().map(e1 -> BillTenantRelevance.of(e.getId(),
                            e1)))
                    .collect(Collectors.toList());
            billTenantRelevanceMapper.insertBatch(relevanceList);
        }
    }

    /**
     * 获取未支付的账单
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<BillVO> getBillList(GetBillListDTO dto) {
        Long count = billMapper.countBill(BillStatus.UNPAID);
        if (count == 0) {
            return new ListQueryVO<>(new ArrayList<>(), 0L);
        }
        Integer pageIndex = dto.getPageIndex();
        Integer pageSize = dto.getPageSize();
        Integer start = (pageIndex - 1) * pageSize;
        List<BillTenantEntity> list = billMapper.getBillList(dto.getStatus(), start, pageSize);
        Collection<BillVO> collection = list.stream().collect(Collectors.groupingBy(Bill::getId, Collectors.collectingAndThen(Collectors.toList(), e -> {
            BillTenantEntity entity = e.stream().findFirst().get();
            String name = e.stream().map(BillTenantEntity::getName).collect(Collectors.joining("、"));
            BillVO vo = BillVO.assemble(entity);
            vo.setTenantInfo(name, entity.getPhone());
            return vo;
        })))
                .values();
        ArrayList<BillVO> vo = ListUtil.toList(collection);
        return new ListQueryVO<>(vo, count);
    }

    /**
     * 获取本月总额
     *
     * @return
     */
    @Override
    public BigDecimal getMonthTotal() {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "water_charge", "electricity_charge", "rent")
                .eq("month(pay_time)", DateUtil.month(DateTime.now()) + 1)
                .and(w -> w.eq("status", BillStatus.PAID).or().eq("status", BillStatus.PRINTED));
        List<Bill> bills = billMapper.selectList(queryWrapper);
        Optional<BigDecimal> total = Optional.empty();
        if (CollectionUtil.isNotEmpty(bills)) {
            BigDecimal totalValue = bills.stream()
                    .map(Bill::totalCharge)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = Optional.of(totalValue);
        }
        return total.orElse(BigDecimal.ZERO);
    }

    /**
     * 获取账单详情
     *
     * @param dto
     * @return
     */
    @Override
    public BillInfoVO getBillInfo(DetailsQueryDTO dto) {
        Bill bill = billMapper.selectById(dto.getId());
        QueryWrapper<BillTenantRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_id", bill.getId());
        billTenantRelevanceMapper.selectList(queryWrapper);
        List<Integer> tenantIds = billTenantRelevanceMapper.selectList(queryWrapper)
                .stream()
                .map(BillTenantRelevance::getTenantId)
                .collect(Collectors.toList());
        BillInfoVO vo = BillInfoVO.assemble(bill);
        JSONArray tenantInfo = tenantProviderService.getTenantByIds(tenantIds);
        for (int i = 0; i < tenantInfo.size(); i++) {
            JSONObject info = tenantInfo.getJSONObject(i);
            TenantInfoVO t = new TenantInfoVO(info.getStr("name"), info.getStr("phone"));
            vo.addTenant(t);
        }
        return vo;
    }

}
