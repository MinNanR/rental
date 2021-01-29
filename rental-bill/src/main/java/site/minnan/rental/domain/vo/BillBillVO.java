package site.minnan.rental.domain.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

@Data
@Builder
public class BillBillVO {

    private Integer id;

    private Integer roomId;

    private String roomNumber;

    private BigDecimal waterCharge;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String time;

    private String livingPeople;

    public static BillBillVO assemble(Bill bill){
        return BillBillVO.builder()
                .id(bill.getId())
                .roomId(bill.getRoomId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterCharge(bill.getWaterCharge())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.totalCharge())
                .time(StrUtil.format("{}年{}月", bill.getYear(), bill.getMonth()))
                .build();
    }
}
