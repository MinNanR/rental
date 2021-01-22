package site.minnan.rental.application.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.provider.RoomProviderService;
import site.minnan.rental.application.provider.TenantProviderService;
import site.minnan.rental.application.provider.UtilityProviderService;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.utils.RedisUtil;
import site.minnan.rental.userinterface.dto.*;

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
        return new UtilityPrice(waterPrice, electricPrice);
    }

    /**
     * 获取账单列表
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<BillVO> getBillList(GetBillListDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> wrapper.eq("house_id", s));
        Optional.ofNullable(dto.getRoomNumber()).ifPresent(s -> wrapper.eq("room_number", s));
        Optional.ofNullable(dto.getYear()).ifPresent(s -> wrapper.eq("year", s));
        Optional.ofNullable(dto.getMonth()).ifPresent(s -> wrapper.eq("month", s));
        Optional<String> status = Optional.ofNullable(dto.getStatus());
        if (status.isPresent()) {
            wrapper.eq("status", status.get());
        } else {
            wrapper.ne("status", BillStatus.INIT)
                    .ne("status", BillStatus.UNSETTLED);
        }
        wrapper.orderByDesc("update_time");
        Page<Bill> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Bill> page = billMapper.selectPage(queryPage, wrapper);
        List<BillVO> list = page.getRecords().stream().map(BillVO::assemble).collect(Collectors.toList());
        long total = page.getTotal();
        return new ListQueryVO<>(list, total);
    }

    /**
     * 获取未结算的账单
     *
     * @param dto
     * @return
     */
    @Override
    public List<BillVO> getUnsettledBill(GetUnsettledBillDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> wrapper.eq("house_id", s));
        wrapper.eq("status", BillStatus.UNSETTLED);
        List<Bill> billList = billMapper.selectList(wrapper);
        return billList.stream()
                .map(BillVO::assemble)
                .collect(Collectors.toList());
    }

    /**
     * 获取未结算的楼层下拉框
     *
     * @param dto
     * @return
     */
    @Override
    public Collection<Integer> getUnsettledFloorDropDown(GetFloorDropDownDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", dto.getHouseId())
                .eq("status", BillStatus.UNSETTLED);
        return billMapper.selectList(wrapper).stream().map(Bill::getFloor).collect(Collectors.toList());
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
                            .floor(roomInfo.getInt("floor"))
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
        }
    }

    /**
     * 获取未支付的账单
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<UnpaidBillVO> getUnpaidBillList(ListQueryDTO dto) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BillStatus.UNPAID);
        queryWrapper.orderByDesc("update_time");
        Page<Bill> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Bill> page = billMapper.selectPage(queryPage, queryWrapper);
        List<Bill> records = page.getRecords();
        Optional<List<UnpaidBillVO>> opt = Optional.empty();
        if (CollectionUtil.isNotEmpty(records)) {
            List<UnpaidBillVO> list = records.stream().map(UnpaidBillVO::assemble).collect(Collectors.toList());
            Set<Integer> ids = records.stream().map(Bill::getRoomId).collect(Collectors.toSet());
            Map<Integer, JSONObject> tenantInfo = tenantProviderService.getTenantInfoByRoomIds(ids);
            list.forEach(e -> {
                JSONObject info = tenantInfo.get(e.getRoomId());
                e.setLivingPeople(info.getStr("name"));
                e.setPhone(info.getStr("phone"));
            });
            opt = Optional.of(list);
        }
        return new ListQueryVO<>(opt.orElseGet(ArrayList::new), page.getTotal());
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
     * 获取已支付且未打印的账单
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<PaidBillVO> getPaidBillList(ListQueryDTO dto) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BillStatus.PAID);
        queryWrapper.orderByAsc("pay_time");
        Page<Bill> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Bill> page = billMapper.selectPage(queryPage, queryWrapper);
        List<Bill> records = page.getRecords();
        Optional<List<PaidBillVO>> opt = Optional.empty();
        if (CollectionUtil.isNotEmpty(records)) {
            List<PaidBillVO> list = records.stream().map(PaidBillVO::assemble).collect(Collectors.toList());
            Set<Integer> ids = records.stream().map(Bill::getRoomId).collect(Collectors.toSet());
            Map<Integer, JSONObject> tenantInfo = tenantProviderService.getTenantInfoByRoomIds(ids);
            list.forEach(e -> {
                JSONObject info = tenantInfo.get(e.getRoomId());
                e.setLivingPeople(info.getStr("name"));
            });
            opt = Optional.of(list);
        }
        return new ListQueryVO<>(opt.orElseGet(ArrayList::new), page.getTotal());
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
        BillInfoVO vo = BillInfoVO.assemble(bill);
        JSONArray tenantInfo = tenantProviderService.getTenantInfoByRoomId(bill.getRoomId());
        for (int i = 0; i < tenantInfo.size(); i++) {
            JSONObject info = tenantInfo.getJSONObject(i);
            TenantInfoVO t = new TenantInfoVO(info.getStr("name"), info.getStr("phone"));
            vo.addTenant(t);
        }
        return vo;
    }
}
