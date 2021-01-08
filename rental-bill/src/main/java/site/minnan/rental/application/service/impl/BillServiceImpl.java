package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UnrecordedBillVO;
import site.minnan.rental.domain.vo.UtilityPrice;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.infrastructure.exception.UnmodifiableException;
import site.minnan.rental.infrastructure.utils.RedisUtil;
import site.minnan.rental.userinterface.dto.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private RedisUtil redisUtil;

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
        billMapper.updateBatch(billList);
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
            if(BillStatus.INIT.equals(bill.getStatus())){
                throw new UnmodifiableException("结算月未结束");
            }else{
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
        billMapper.update(null,wrapper);
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
        wrapper.eq("house_id",dto.getHouseId())
                .eq("year", dto.getYear())
                .eq("month", dto.getMonth())
                .eq("status", BillStatus.UNRECORDED);
        List<Bill> billList = billMapper.selectList(wrapper);
        return billList.stream().map(Bill::getFloor).distinct().collect(Collectors.toList());
    }
}
