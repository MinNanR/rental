package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 水电单状态
 * @author Minnan on 2021/01/06
 */
public enum UtilityStatus {

    UNSETTLED("UNSETTLED", "未出单"),
    SETTLED("SETTLED", "已出单");

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
