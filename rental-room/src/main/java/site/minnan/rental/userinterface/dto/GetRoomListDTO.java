package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 房间列表查询参数
 * @author Minnan on 2020/12/29
 */
@Data
public class GetRoomListDTO extends ListQueryDTO{

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    private Integer floor;
}
