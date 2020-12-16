package site.minnan.rental.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.minnan.rental.domain.aggretes.AuthUser;

/**
 * 用户信息值对象
 * created by Minnan on 2020/12/16
 */
@Builder
@Getter
@Setter
public class AuthUserVO {

    /**
     * id值
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 角色名称
     */
    private String roleName;

    public static AuthUserVO assemble(AuthUser user) {
        return AuthUserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .roleName(user.getRoleName())
                .build();
    }
}
