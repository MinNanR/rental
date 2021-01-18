package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房客迁移房屋参数
 * @author Minnan on 2020/01/04
 */
@Data
public class TenantMoveDTO {

    @NotNull(message = "未指定房间")
    private Integer roomId;

    @NotNull(message = "未指定房屋")
    private Integer houseId;

    @NotEmpty(message = "未指定房屋")
    private String houseName;

    @NotEmpty(message = "未指定房间")
    private String roomNumber;

    @NotEmpty(message = "未指定要迁移的房客")
    private List<Integer> tenantIdList;

}
