package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

@Data
@Builder
public class PaidBillVO {

    private Integer id;

    private Integer roomId;

    private String roomNumber;

    private BigDecimal waterCharge;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String time;

    private String livingPeople;

    private String payTime;

    private String paymentMethod;

    public static PaidBillVO assemble(Bill bill){
        return PaidBillVO.builder()
                .id(bill.getId())
                .roomId(bill.getRoomId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterCharge(bill.getWaterCharge())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.totalCharge())
                .time(StrUtil.format("{}年{}月", bill.getYear(), bill.getMonth()))
                .payTime(DateUtil.format(bill.getPayTime(), "yyyy-MM-dd"))
                .paymentMethod(bill.getPaymentMethod().getMethod())
                .build();
    }
}
