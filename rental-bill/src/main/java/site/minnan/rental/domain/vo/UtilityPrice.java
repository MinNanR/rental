package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 水电单价
 * @author Minnan on 2021/1/8
 */
@Data
@AllArgsConstructor
public class UtilityPrice {

    private BigDecimal waterPrice;

    private BigDecimal electricityPrice;

    private Map<String, Integer> accessCardPrice;

    public Integer getAccessCardPrice(String houseName){
        return accessCardPrice.get(houseName);
    }
}
