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

    private String updateUserName;

    private String updateTime;

    public static BillVO assemble(Bill bill){
        return BillVO.builder()
                .id(bill.getId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterUsage(bill.getWaterUsage())
                .waterCharge(bill.getWaterCharge())
                .electricityUsage(bill.getElectricityUsage())
                .electricityCharge(bill.getElectricityCharge())
                .updateUserName(bill.getUpdateUserName())
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd"))
                .build();
    }
}
