package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AddUtilityDTO {

    @NotNull(message = "未指定房间")
    private Integer roomId;

    private String roomNumber;

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    private String houseName;

    @NotNull(message = "未填写水表计数")
    private BigDecimal water;

    @NotNull(message = "未填写电表计数")
    private BigDecimal electricity;
}
