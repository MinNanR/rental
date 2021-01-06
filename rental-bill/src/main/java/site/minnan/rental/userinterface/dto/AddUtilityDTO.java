package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 添加水电参数
 * @author Minnan on 2021/01/06
 */
@Data
public class AddUtilityDTO {

    @NotNull(message = "未指定房间")
    private Integer roomId;

    @NotEmpty(message = "未指定房间")
    private String roomNumber;

    private BigDecimal water;

    private BigDecimal electricity;
}
