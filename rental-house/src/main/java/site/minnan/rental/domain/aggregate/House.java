package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.entity.JwtUser;

import java.sql.Timestamp;

/**
 * 房子实体类
 * @author Minnan on 2020/12/17
 */
@TableName("rental_house")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class House {

    /**
     * id值
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房屋简称
     */
    private String houseName;

    /**
     * 房子地址
     */
    private String address;

    /**
     * 管理人姓名
     */
    private String directorName;

    /**
     * 管理人电话号码
     */
    private String directorPhone;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人真实姓名
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
     * 更新人真实姓名
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public void setCreateUser(JwtUser user){
        this.createUserId = user.getId();
        this.createUserName = user.getRealName();
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }
}
