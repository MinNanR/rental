package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 启用或禁用用户参数
 * @author Minnan on 2020/12/17
 */
@Data
public class UserEnabledDTO {

    @NotNull(message = "id不能为空")
    private Integer id;
}
