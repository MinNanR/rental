package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

@Data
@Builder
public class UnpaidBillVO {

    private Integer id;

    private Integer roomId;

    private String roomNumber;

    private BigDecimal waterCharge;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String time;

    private String livingPeople;

    private String phone;

    private String updateTime;

    public static UnpaidBillVO assemble(Bill bill){
        return UnpaidBillVO.builder()
                .id(bill.getId())
                .roomId(bill.getRoomId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterCharge(bill.getWaterCharge())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.getWaterCharge().add(bill.getElectricityCharge()).add(BigDecimal.valueOf(bill.getRent())))
                .time(StrUtil.format("{}年{}月", bill.getYear(), bill.getMonth()))
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd"))
                .build();
    }

}
