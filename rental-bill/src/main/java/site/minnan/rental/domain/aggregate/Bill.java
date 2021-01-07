package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.infrastructure.enumerate.BillStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 账单
 *
 * @author Minnan on 2021/01/06
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("rental_bill")
public class Bill {

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
     * 所属房间简称
     */
    private String houseName;

    /**
     * 房间id
     */
    private Integer roomId;

    /**
     * 房间编号
     */
    private String roomNumber;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 水费
     */
    private BigDecimal waterCharge;

    /**
     * 电费
     */
    private BigDecimal electricityCharge;

    /**
     * 房租
     */
    private Integer rent;

    /**
     * 结束日期
     */
    private Date completedDate;

    /**
     * 水电单id
     */
    private Integer utilityId;

    /**
     * 账单状态
     */
    private BillStatus status;

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

    public void setCreateUser(Integer userId, String userName, Timestamp time){
        this.createUserId = userId;
        this.createUserName = userName;
        this.createTime = time;
        this.updateUserId = userId;
        this.updateUserName = userName;
        this.updateTime = time;
    }
}
