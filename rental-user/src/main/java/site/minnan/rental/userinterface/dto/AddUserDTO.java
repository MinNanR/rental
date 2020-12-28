package site.minnan.rental.userinterface.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotEmpty;

/**
 * 添加用户参数
 * @author Minnan on 2020/12/16
 */
@Data
@AllArgsConstructor
@Builder
public class AddUserDTO {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "真实姓名不能为空")
    private String realName;

    private String role;

    private String phone;
}
