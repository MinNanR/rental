package site.minnan.rental.userinterface.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 设置水电单价参数
 *
 * @author Minnan on 2021/1/8
 */
@Data
public class SetUtilityPriceDTO {

    private BigDecimal waterPrice;

    private BigDecimal electricityPrice;
}
