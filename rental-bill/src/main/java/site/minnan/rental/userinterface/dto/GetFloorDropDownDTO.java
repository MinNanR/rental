package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取楼层下拉框参数
 * @author Minnan on 2021/01/07
 */
@Data
public class GetFloorDropDownDTO {

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    private Integer year;

    private Integer month;
}
