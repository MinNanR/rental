package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 水电单价
 * @author Minnan on 2021/1/8
 */
@Data
@AllArgsConstructor
public class UtilityPrice {

    private BigDecimal waterPrice;

    private BigDecimal electricityPrice;
}
