package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.minnan.rental.domain.aggregate.AuthUser;

/**
 * 用户信息值对象
 * @author Minnan on 2020/12/16
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

    private Integer enabled;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新时间
     */
    private String updateUserName;

    public static AuthUserVO assemble(AuthUser user) {
        return AuthUserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .roleName(user.getRole().getRoleName())
                .enabled(user.getEnabled())
                .updateUserName(user.getUpdateUserName())
                .updateTime(DateUtil.format(user.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }
}
