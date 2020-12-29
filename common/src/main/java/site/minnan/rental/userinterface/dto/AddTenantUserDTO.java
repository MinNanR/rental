package site.minnan.rental.userinterface.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建房客用户参数
 * @author Minnan on 2020/12/29
 */
@Data
@Builder
public class AddTenantUserDTO implements Serializable {

    private String realName;

    private String phone;

    private Integer userId;

    private String userName;
}
