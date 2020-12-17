package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 密码登录参数
 * @author Minnan on 2020/12/16
 */
@Data
public class PasswordLoginDTO {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

}
