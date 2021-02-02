package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

@Data
@Builder
public class BillVO {

    private Integer id;

    private Integer roomId;

    private String roomNumber;

    private BigDecimal waterCharge;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String time;

    @Setter
    private String livingPeople;

    @Setter
    private String phone;

    private String updateTime;

    private String status;

    private String statusCode;

    private String type;

    private String typeCode;

    public static BillVO assemble(Bill bill){
        return BillVO.builder()
                .id(bill.getId())
                .roomId(bill.getRoomId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterCharge(bill.getWaterCharge())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.totalCharge())
                .status(bill.getStatus().getStatus())
                .statusCode(bill.getStatus().getValue())
                .type(bill.getType().getType())
                .typeCode(bill.getType().getValue())
                .time(StrUtil.format("{}年{}月", bill.getYear(), bill.getMonth()))
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd"))
                .build();
    }

    public void setTenantInfo(String livingPeople,String phone){
        this.livingPeople = livingPeople;
        this.phone = phone;
    }

}
