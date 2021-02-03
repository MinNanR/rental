package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 账单状态枚举
 * @author Minnan on 2021/01/06
 */
public enum  BillStatus {

    INIT("INIT","正在使用"),
    UNCONFIRMED("UNCONFIRMED", "未确认"),
    UNPAID("UNPAID", "未支付"),
    PAID("PAID","已支付");
//    PRINTED("PRINTED", "已打印收据");

    @EnumValue
    private final String value;

    private final String status;

    BillStatus(String value, String status) {
        this.value = value;
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }
}
