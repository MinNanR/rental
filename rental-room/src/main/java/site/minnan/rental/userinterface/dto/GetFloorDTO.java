package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 小程序查询所有房间参数
 * @author Minnan on 2021/1/21
 */
@Data
public class GetFloorDTO {

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    private String roomNumber;
}
