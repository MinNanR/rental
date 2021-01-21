package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.infrastructure.enumerate.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 账单详情数据（小程序用）
 *
 * @author Minnan on 2021/1/20
 */
@Data
@Builder
public class BillInfoVO {

    private Integer id;

    private Integer roomId;

    private String houseName;

    private String roomNumber;

    private List<TenantInfoVO> tenantList;

    private BigDecimal waterUsage;

    private BigDecimal waterCharge;

    private BigDecimal electricityUsage;

    private BigDecimal electricityCharge;

    private Integer rent;

    private BigDecimal totalCharge;

    private String updateTime;

    private String payTime;

    private String paymentMethod;

    private String paymentMethodCode;

    private String status;

    private String statusCode;

    public static BillInfoVO assemble(Bill bill) {
        return BillInfoVO.builder()
                .id(bill.getId())
                .houseName(bill.getHouseName())
                .roomId(bill.getRoomId())
                .roomNumber(bill.getRoomNumber())
                .waterUsage(bill.getWaterUsage())
                .waterCharge(bill.getWaterCharge())
                .electricityUsage(bill.getElectricityUsage())
                .electricityCharge(bill.getElectricityCharge())
                .rent(bill.getRent())
                .totalCharge(bill.totalCharge())
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd"))
                .payTime(DateUtil.format(bill.getPayTime(), "yyyy-MM-dd"))
                .paymentMethod(Optional.ofNullable(bill.getPaymentMethod()).map(PaymentMethod::getMethod).orElse(""))
                .paymentMethodCode(Optional.ofNullable(bill.getPaymentMethod()).map(PaymentMethod::getValue).orElse(""))
                .status(bill.getStatus().getStatus())
                .statusCode(bill.getStatus().getValue())
                .tenantList(new ArrayList<>())
                .build();
    }

    public void addTenant(TenantInfoVO vo){
        tenantList.add(vo);
    }
}
