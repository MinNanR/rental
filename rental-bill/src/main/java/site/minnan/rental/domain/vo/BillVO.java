package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

/**
 * @author Minnan on 2021/1/12
 */
@Data
@Builder
public class BillVO {

    private Integer id;

    private String roomNumber;

    private BigDecimal waterUsage;

    private BigDecimal waterCharge;

    private BigDecimal electricityUsage;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String time;

    private String updateUserName;

    private String updateTime;

    public static BillVO assemble(Bill bill) {
        return BillVO.builder()
                .id(bill.getId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterUsage(bill.getWaterUsage())
                .waterCharge(bill.getWaterCharge())
                .electricityUsage(bill.getElectricityUsage())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.totalCharge())
                .time(StrUtil.format("{}年{}月", bill.getYear(), bill.getMonth()))
                .updateUserName(bill.getUpdateUserName())
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }
}
