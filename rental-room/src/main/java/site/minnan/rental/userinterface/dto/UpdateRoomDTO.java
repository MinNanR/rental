package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新房间参数
 *
 * @author Minnan on 2020/12/29
 */
@Data
public class UpdateRoomDTO {

    @NotNull(message = "未指定房间")
    private Integer id;

    private String roomNumber;

    private Integer floor;

    private Integer price;
}
