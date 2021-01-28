package site.minnan.rental.domain.vo;

import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Room;

import java.math.BigDecimal;

/**
 * 指定楼层的房间，并附带当前度数
 * @author Minnan on 2021/1/28
 */
@Data
public class UtilityInitVO {

    private Integer roomId;

    private String roomNumber;

    private BigDecimal currentWater;

    private BigDecimal currentElectricity;
}
