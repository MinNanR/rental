package site.minnan.rental.domain.entity;

import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

/**
 * 账单所有信息
 *
 * @author Minnan on 2021/01/30
 */
@Data
public class BillDetails extends Bill {

    private BigDecimal waterStart;

    private BigDecimal waterEnd;

    private BigDecimal electricityStart;

    private BigDecimal electricityEnd;

    public void settleWater(BigDecimal price) {
        super.settleWater(waterStart, waterEnd, price);
    }

    public void settleElectricity(BigDecimal price) {
        super.settleElectricity(electricityStart, electricityEnd, price);
    }
}
