package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.Gender;
import site.minnan.rental.infrastructure.enumerate.TenantStatus;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 房客信息
 *
 * @author Minnan on 2020/12/21
 */
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@TableName("rental_tenant")
public class Tenant {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房客姓名
     */
    private String name;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 生日（年月日）
     */
    private Date birthday;

    /**
     * 籍贯(省份）
     */
    private String hometownProvince;

    /**
     * 籍贯（城市）
     */
    private String hometownCity;

    /**
     * 身份证号码
     */
    private String identificationNumber;


    /**
     * 所住房屋id
     */
    private Integer houseId;

    /**
     * 所住房屋简称
     */
    private String houseName;

    /**
     * 所住房间id
     */
    private Integer roomId;

    /**
     * 房间编号
     */
    private String roomNumber;

    /**
     * 房客状态
     */
    private TenantStatus status;

    /**
     * 用户账号id
     */
    @Setter
    private Integer userId;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新人id
     */
    private Integer updateUserId;

    /**
     * 更新人姓名
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public void setCreateUser(JwtUser user) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createUserId = user.getId();
        this.createUserName = user.getRealName();
        this.createTime = currentTime;
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
        this.updateTime = currentTime;
    }

    public void setUpdateUser(JwtUser user) {
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }
}
