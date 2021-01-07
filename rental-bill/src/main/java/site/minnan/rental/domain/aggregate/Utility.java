package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.AddUtilityBatchDTO;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * 水电账单
 *
 * @author Minnan on 2021/01/06
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("rental_utility")
public class Utility {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 所属房屋id
     */
    private Integer houseId;

    /**
     * 所属房屋简称
     */
    private String houseName;

    /**
     * 所属房间id
     */
    private Integer roomId;

    /**
     * 所属房间编号
     */
    private String roomNumber;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 用水量
     */
    private BigDecimal water;

    /**
     * 用电量
     */
    private BigDecimal electricity;

    /**
     * 账单id
     */
    private Integer billId;

    /**
     * 状态
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

    public static Utility assemble(AddUtilityDTO utilityDTO, AddUtilityBatchDTO batchDTO) {
        BigDecimal water = Optional.ofNullable(utilityDTO.getWater()).orElse(BigDecimal.ZERO);
        BigDecimal electricity = Optional.ofNullable(utilityDTO.getElectricity()).orElse(BigDecimal.ZERO);
        return Utility.builder()
                .year(batchDTO.getYear())
                .month(batchDTO.getMonth())
                .houseId(batchDTO.getHouseId())
                .houseName(batchDTO.getHouseName())
                .roomId(utilityDTO.getRoomId())
                .roomNumber(utilityDTO.getRoomNumber())
                .floor(batchDTO.getFloor())
                .water(water)
                .electricity(electricity)
                .status(UtilityStatus.UNSETTLED)
                .build();
    }

    public void setCreateUser(JwtUser user) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        createUserId = user.getId();
        createUserName = user.getRealName();
        createTime = current;
        updateUserId = user.getId();
        updateUserName = user.getRealName();
        updateTime = current;
    }

    public void setCreateUser(Integer userId, String userName, Timestamp time){
        this.createUserId = userId;
        this.createUserName = userName;
        this.createTime = time;
        this.updateUserId = userId;
        this.updateUserName = userName;
        this.updateTime = time;
    }
}
