package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 登记水电用量情况
 *
 * @author Minnan on 2021/01/08
 */
@Data
public class RecordUtilityDTO {

    @NotNull(message = "未指定要记录的账单")
    private Integer id;

    @NotNull(message = "用水量未填写")
    private BigDecimal waterUsage;

    @NotNull(message = "用电量未填写")
    private BigDecimal electricityUsage;
}
