package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 登记添加房客参数
 *
 * @author Minnan on 2021/1/25
 */
@Data
public class RegisterAddTenantDTO {

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotNull(message = "未指定房屋")
    private String houseName;

    @NotNull(message = "未指定房间")
    private Integer roomId;

    @NotEmpty(message = "未指定房间")
    private String roomNumber;

    private List<AddTenantDTO> tenantList;
}
