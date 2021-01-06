package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量登记水电参数
 *
 * @author Minnan on 2020/01/06
 */
@Data
public class AddUtilityBatchDTO {

    @NotNull(message = "年份未填写")
    private Integer year;

    @NotNull(message = "月份未填写")
    private Integer month;

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotEmpty(message = "未指定房屋")
    private String houseName;

    @NotNull(message = "未指定楼层")
    private Integer floor;

    List<AddUtilityDTO> utilityList;
}
