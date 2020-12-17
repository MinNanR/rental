package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新用户信息参数
 * @author Minnan on 2020/12/17
 */
@Data
public class UpdateUserDTO {

    @NotNull(message = "未指定要修改的用户")
    private Integer id;

    private String password;

    private String phone;

    private String realName;
}
