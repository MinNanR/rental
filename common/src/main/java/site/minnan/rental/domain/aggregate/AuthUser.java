package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.Role;

import java.sql.Timestamp;

/**
 * 用户实体类
 *
 * @author Minnan on 2020/12/16
 */
@TableName("auth_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 角色
     */
    private Role role;

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

    /**
     * 启用状态
     */
    public final static Integer ENABLE = 1;

    /**
     * 禁用状态
     */
    public final static Integer DISABLE = 0;

    public void setCreateUser(JwtUser user) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        this.createUserId = user.getId();
        this.createUserName = user.getRealName();
        this.createTime = current;
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
        this.updateTime = current;
    }

    public void setCreateUser(Integer userId, String userName){
        Timestamp current = new Timestamp(System.currentTimeMillis());
        this.createUserId = userId;
        this.createUserName = userName;
        this.createTime = current;
        this.updateUserId = userId;
        this.updateUserName = userName;
        this.updateTime = current;
    }

    public void setUpdateUser(JwtUser user) {
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
    }
}
