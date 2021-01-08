package site.minnan.rental.domain.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

/**
 * @author Minnan on 2021/1/8
 */
@Data
@Builder
public class UnrecordedBillVO {

    private Integer id;

    private String roomNumber;

    private BigDecimal waterUsage;

    private BigDecimal electricityUsage;

    public static UnrecordedBillVO assemble(Bill bill) {
        return UnrecordedBillVO.builder()
                .id(bill.getId())
                .roomNumber(StrUtil.format("{}-{}", bill.getHouseName(), bill.getRoomNumber()))
                .waterUsage(null)
                .electricityUsage(null)
                .build();
    }
}
