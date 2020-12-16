package site.minnan.rental.domain.aggretes;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.infrastructure.enumerate.Role;

import java.sql.Timestamp;

/**
 * 用户实体类
 * created by Minnan on 2020/12/16
 */
@TableName("auth_user")
@Data
@Builder
public class AuthUser {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 角色（英文）
     */
    private String role;

    /**
     * 是否启用（0-禁用，1-启用）
     */
    private Integer enabled;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 真实姓名
     */
    private String phone;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 更新人id
     */
    private Integer updateUserId;

    /**
     * 更新人姓名
     */
    private String updateUserName;

    public void role(Role role){
        this.roleName = role.roleName();
        this.roleId = role.roleId();
        this.role = role.name();
    }

    public void createUser(JwtUser user){
        this.createUserId = user.getId();
        this.createUserName = user.getRealName();
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
    }
}
