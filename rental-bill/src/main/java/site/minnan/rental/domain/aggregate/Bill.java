package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.BillType;
import site.minnan.rental.infrastructure.enumerate.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

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
     * 用水量
     */
    private BigDecimal waterUsage;

    /**
     * 水费
     */
    private BigDecimal waterCharge;

    /**
     * 用电量
     */
    private BigDecimal electricityUsage;

    /**
     * 电费
     */
    private BigDecimal electricityCharge;

    /**
     * 门禁卡数量
     */
    private Integer accessCardQuantity;

    /**
     * 门禁卡收费
     */
    private Integer accessCardCharge;

    /**
     * 押金
     */
    private Integer deposit;

    /**
     * 房租
     */
    private Integer rent;

    /**
     * 开始使用的水电单id
     */
    private Integer utilityStartId;

    /**
     * 结束时使用的水电单id
     */
    @Setter
    private Integer utilityEndId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 结束日期
     */
    private Date completedDate;

    /**
     * 支付时间
     */
    private Timestamp payTime;

    /**
     * 支付方式
     */
    private PaymentMethod paymentMethod;

    /**
     * 账单状态
     */
    private BillStatus status;

    /**
     * 账单类型
     */
    private BillType type;

    /**
     * 收据url地址
     */
    private String receiptUrl;

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

    public void setCreateUser(Integer userId, String userName, Timestamp time) {
        this.createUserId = userId;
        this.createUserName = userName;
        this.createTime = time;
        this.updateUserId = userId;
        this.updateUserName = userName;
        this.updateTime = time;
    }

    public void setUpdateUser(JwtUser jwtUser) {
        this.updateUserId = jwtUser.getId();
        this.updateUserName = jwtUser.getRealName();
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    public void settle(BigDecimal waterPrice, BigDecimal electricityPrice) {
        this.waterCharge = this.waterUsage.multiply(waterPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.electricityCharge = this.electricityUsage.multiply(electricityPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.status = BillStatus.UNPAID;
    }

    /**
     * 结算用水情况
     *
     * @param start 水表开始的度数
     * @param end   水表结束时的度数
     * @param price 水费单价
     */
    public void settleWater(BigDecimal start, BigDecimal end, BigDecimal price) {
        this.waterUsage = end.subtract(start);
        BigDecimal waterUsage = BigDecimal.ONE.compareTo(this.waterUsage) > 0 ? BigDecimal.ONE : this.waterUsage;
        this.waterCharge = waterUsage.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 结算用电情况
     *
     * @param start 电表开始的度数
     * @param end   电表结算的读书
     * @param price 电费单价
     */
    public void settleElectricity(BigDecimal start, BigDecimal end, BigDecimal price) {
        this.electricityUsage = end.subtract(start);
        this.electricityCharge = this.waterUsage.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal totalCharge() {
        BigDecimal totalCharge = BigDecimal.ZERO;
        switch (type) {
            case MONTHLY:
                totalCharge = waterCharge.add(electricityCharge).add(BigDecimal.valueOf(rent));
                break;
            case CHECK_IN:
                totalCharge = BigDecimal.valueOf(rent + +deposit + accessCardCharge);
                break;
            default:
        }
        return totalCharge;
    }

    public void unsettled() {
        this.status = BillStatus.UNSETTLED;
    }

    public void surrenderCompleted(Date time) {
        this.completedDate = time;
    }
}
