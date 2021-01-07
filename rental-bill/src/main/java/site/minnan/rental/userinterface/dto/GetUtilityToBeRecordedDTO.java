package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取需要记录水电的房间
 * @author Minnan on 2021/01/06
 */
@Data
public class GetUtilityToBeRecordedDTO {

    @NotNull(message = "未选择房屋")
    private Integer houseId;

    @NotNull(message = "未选择年份")
    private Integer year;

    @NotNull(message = "未选择月份")
    private Integer month;

    @NotNull(message = "未选择楼层")
    private Integer floor;
}
