package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取用于
 * @author Minnan on 2021/1/28
 */
@Data
public class GetRoomToRecordDTO {

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotNull(message = "未指定楼层")
    private Integer floor;
}
