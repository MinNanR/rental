package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取未登记水电的账单
 * @author Minnan on 2021/1/8
 */
@Data
public class GetUnrecordedBillDTO {

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotNull(message = "未指定楼层")
    private Integer floor;

    @NotNull(message = "未指定年份")
    private Integer year;

    @NotNull(message = "未指定月份")
    private Integer month;
}
