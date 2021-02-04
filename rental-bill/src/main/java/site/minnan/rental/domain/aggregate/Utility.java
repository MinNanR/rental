package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 水电单实体类
 * @author Minnan on 2021/1/22
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rental_utility")
public class Utility {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房间id
     */
    private Integer roomId;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 房屋id
     */
    private Integer houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 水表计数
     */
    private BigDecimal water;

    /**
     * 电表计数
     */
    private BigDecimal electricity;

    /**
     * 状态（记录中的水电单可以修改，已归档的水电单不可修改）
     */
    private UtilityStatus status;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人名称
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
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public void setCreateUser(JwtUser user){
        Timestamp current = new Timestamp(System.currentTimeMillis());
        setCreateUser(user, current);
    }

    public void setCreateUser(JwtUser user, Timestamp current){
        createUserId = user.getId();
        createUserName = user.getRealName();
        createTime = current;
        updateUserId = user.getId();
        updateUserName = user.getRealName();
        updateTime = current;
    }

    public void setUpdateUser(JwtUser user, Timestamp current){
        updateUserId = user.getId();
        updateUserName = user.getRealName();
        updateTime = current;
    }

    public void setUpdateUser(JwtUser user){
        Timestamp current = new Timestamp(System.currentTimeMillis());
        setUpdateUser(user, current);
    }

    public static Utility assemble(AddUtilityDTO dto){
       return Utility.builder()
               .roomId(dto.getRoomId())
               .roomNumber(dto.getRoomNumber())
               .houseId(dto.getHouseId())
               .houseName(dto.getHouseName())
               .water(dto.getWater())
               .electricity(dto.getElectricity())
               .status(UtilityStatus.RECORDING)
               .build();
    }

}
