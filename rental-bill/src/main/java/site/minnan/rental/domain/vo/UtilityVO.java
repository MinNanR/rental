package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Utility;

import java.math.BigDecimal;

/**
 * 水电记录
 *
 * @author Minnan on 2021/1/8
 */
@Data
@Builder
public class UtilityVO {

    private Integer id;

    private String houseName;

    private String roomNumber;

    private BigDecimal water;

    private BigDecimal electricity;

    private String status;

    private String statusCode;

    private String updateUserName;

    private String updateTime;

    public static UtilityVO assemble(Utility utility) {
        return UtilityVO.builder()
                .id(utility.getId())
                .houseName(utility.getHouseName())
                .roomNumber(utility.getRoomNumber())
                .water(utility.getWater())
                .electricity(utility.getElectricity())
                .status(utility.getStatus().getStatus())
                .statusCode(utility.getStatus().getValue())
                .updateUserName(utility.getUpdateUserName())
                .updateTime(DateUtil.format(utility.getUpdateTime(), "yyyy年MM月dd日 HH:mm"))
                .build();
    }

}
