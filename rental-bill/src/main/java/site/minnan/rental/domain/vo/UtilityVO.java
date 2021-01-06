package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Utility;

import java.math.BigDecimal;

/**
 * 水电记录列表
 * @author Minnan on 2021/01/05
 */
@Data
@Builder
public class UtilityVO {

    private Integer id;

    private String room;

    private BigDecimal water;

    private BigDecimal electricity;

    private String updateUserName;

    private String updateTime;

    public static UtilityVO assemble(Utility utility){
        return UtilityVO.builder()
                .id(utility.getId())
                .room(StrUtil.format("{}-{}", utility.getHouseName(), utility.getRoomNumber()))
                .water(utility.getWater())
                .electricity(utility.getElectricity())
                .updateUserName(utility.getUpdateUserName())
                .updateTime(DateUtil.format(utility.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }
}
