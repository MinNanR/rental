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
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.infrastructure.exception.UnmodifiableException;
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

    @Reference
    private RoomProviderService roomProviderService;

    /**
     * 登记水电用量
     *
     * @param recordList
     */
    @Override
    public void recordUtility(List<RecordUtilityDTO> recordList) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Bill> billList = recordList.stream()
                .map(Bill::assemble)
                .peek(e -> e.setUpdateUser(jwtUser))
                .collect(Collectors.toList());
        billMapper.updateUtilityBatch(billList);
    }

    /**
     * 获取未登记水电的房间
     *
     * @param dto
     * @return
     */
    @Override
    public List<UnrecordedBillVO> getUnrecordedBill(GetUnrecordedBillDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", dto.getHouseId())
                .eq("floor", dto.getFloor())
                .eq("year", dto.getYear())
                .eq("month", dto.getMonth())
                .eq("status", BillStatus.UNRECORDED);
        List<Bill> bills = billMapper.selectList(wrapper);
        return bills.stream().map(UnrecordedBillVO::assemble).collect(Collectors.toList());
    }

    /**
     * 修改水电量
     *
     * @param dto
     */
    @Override
    public void updateUtility(UpdateUtilityDTO dto) {
        Bill bill = billMapper.selectById(dto.getId());
        if (bill == null) {
            throw new EntityNotExistException("账单不存在");
        }
        if (!BillStatus.UNRECORDED.equals(bill.getStatus()) && !BillStatus.UNSETTLED.equals(bill.getStatus())) {
            if (BillStatus.INIT.equals(bill.getStatus())) {
                throw new UnmodifiableException("结算月未结束");
            } else {
                throw new UnmodifiableException("当前账单已结算");
            }
        }
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateWrapper<Bill> wrapper = new UpdateWrapper<>();
        Optional.ofNullable(dto.getWaterUsage()).ifPresent(s -> wrapper.set("water_usage", s));
        Optional.ofNullable(dto.getElectricityUsage()).ifPresent(s -> wrapper.set("electricity_usage", s));
        wrapper.set("update_user_id", jwtUser.getId())
                .set("update_user_name", jwtUser.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()));
        billMapper.update(null, wrapper);
    }

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
     * 获取水电记录列表
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<UtilityVO> getUtilityList(GetUtilityListDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> wrapper.eq("house_id", s));
        Optional.ofNullable(dto.getRoomNumber()).ifPresent(s -> wrapper.eq("room_number", s));
        Optional.ofNullable(dto.getYear()).ifPresent(s -> wrapper.eq("year", s));
        Optional.ofNullable(dto.getMonth()).ifPresent(s -> wrapper.eq("month", s));
        Optional.ofNullable(dto.getStatus()).ifPresent(s -> wrapper.eq("status", s));
        wrapper.ne("status", BillStatus.INIT)
                .ne("status", BillStatus.UNRECORDED);
        Page<Bill> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Bill> page = billMapper.selectPage(queryPage, wrapper);
        List<UtilityVO> list = page.getRecords().stream().map(UtilityVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(list, page.getTotal());
    }

    /**
     * 获取楼层下拉框
     *
     * @param dto
     * @return
     */
    @Override
    public Collection<Integer> getFloorDropDown(GetFloorDropDownDTO dto) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", dto.getHouseId())
                .eq("year", dto.getYear())
                .eq("month", dto.getMonth())
                .eq("status", BillStatus.UNRECORDED);
        List<Bill> billList = billMapper.selectList(wrapper);
        return billList.stream().map(Bill::getFloor).distinct().collect(Collectors.toList());
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
                    .ne("status", BillStatus.UNRECORDED)
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
        UtilityPrice price = getUtilityPrice();
        BigDecimal waterPrice = price.getWaterPrice();
        BigDecimal electricityPrice = price.getElectricityPrice();
        return billList.stream()
                .peek(e -> e.settle(waterPrice, electricityPrice))
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
    public void setBillUnrecorded() {
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
            List<Integer> initializedBillIdList = initBillList.stream().map(Bill::getId).collect(Collectors.toList());
            UpdateWrapper<Bill> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status", BillStatus.UNRECORDED)
                    .set("update_user_id", 0)
                    .set("update_user_name", "系统")
                    .set("update_time", currentTime)
                    .in("id", initializedBillIdList);
            billMapper.update(null, updateWrapper);

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
                            .status(BillStatus.INIT)
                            .build();
                    newBill.setCreateUser(0, "系统", currentTime);
                    newBillList.add(newBill);
                }
            }
            billMapper.insertBatch(newBillList);
        }
    }
}
