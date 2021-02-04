package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 修改水电参数
 * @author Minnan on 2021/1/22
 */
@Data
public class UpdateUtilityDTO {

    @NotNull(message = "未指定要修改的记录")
    private Integer id;

    private BigDecimal water;

    private BigDecimal electricity;
}
