package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 修改水电用量参数
 * @author Minnan on 2021/1/8
 */
@Data
public class UpdateUtilityDTO {

    @NotNull(message = "未指定要修改的记录")
    private Integer id;

    private BigDecimal waterUsage;

    private BigDecimal electricityUsage;
}
