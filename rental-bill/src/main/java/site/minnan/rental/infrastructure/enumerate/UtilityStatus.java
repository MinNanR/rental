package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 水电单状态
 * @author Minnan on 2021/01/06
 */
public enum UtilityStatus {

    USING("USING","正在使用"),
    TO_BE_RECORDED("TO_BE_RECORDED", "待登记"),
    UNSETTLED("UNSETTLED", "未结算"),
    SETTLED("SETTLED", "已结算");

    @EnumValue
    private final String value;

    private final String status;

    UtilityStatus(String value, String status) {
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
