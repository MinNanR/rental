package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 检查房间号码是否已使用参数
 * @author Minnan on 2020/12/29
 */
@Data
public class CheckRoomNumberDTO {

    private Integer id;

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotNull(message = "房间编号不能为空")
    private String roomNumber;
}
