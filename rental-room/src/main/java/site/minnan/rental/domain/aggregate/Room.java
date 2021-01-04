package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;

import java.sql.Timestamp;

/**
 * 房间实体类
 *
 * @author Minnan on 2020/12/29
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rental_room")
public class Room {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房屋id
     */
    private Integer houseId;

    /**
     * 房屋简称
     */
    private String houseName;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 每月租金
     */
    private Integer price;

    /**
     * 房间状态
     */
    private RoomStatus status;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人名称
     */
    private String  createUserName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改人id
     */
    private Integer updateUserId;

    /**
     * 修改人名称
     */
    private String  updateUserName;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    public void setCreateUser(JwtUser user){
        Timestamp current = new Timestamp(System.currentTimeMillis());
        this.createUserId = user.getId();
        this.createUserName = user.getRealName();
        this.createTime = current;
        this.updateUserId = user.getId();
        this.updateUserName = user.getRealName();
        this.updateTime = current;
    }
}
