package site.minnan.rental.userinterface.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 设置水电单价参数
 *
 * @author Minnan on 2021/1/8
 */
@Data
public class SetUtilityPriceDTO {

    private BigDecimal waterPrice;

    private BigDecimal electricityPrice;

    private Map<String, Integer> accessCardPrice;
}
