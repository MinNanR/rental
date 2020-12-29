package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 创建房间参数
 * @author Minnan on 2020/12/29
 */
@Data
public class AddRoomDTO {

    @NotNull(message = "未指定所属房屋")
    private Integer houseId;

    @NotEmpty(message = "房间编号不能为空")
    private String roomNumber;

    private Integer floor;

    private Integer price;
}
