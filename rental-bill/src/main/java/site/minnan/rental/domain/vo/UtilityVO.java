package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;

/**
 * 水电记录
 * @author Minnan on 2021/1/8
 */
@Data
@Builder
public class UtilityVO {

    private Integer id;

    private BigDecimal water;

    private BigDecimal electricity;

    private String updateUserName;

    private String updateTime;

    public static UtilityVO assemble(Bill bill){
        return UtilityVO.builder()
                .id(bill.getId())
                .water(bill.getWaterUsage())
                .electricity(bill.getElectricityUsage())
                .updateUserName(bill.getUpdateUserName())
                .updateTime(DateUtil.format(bill.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }

}
