package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 *
 * @author Minnan on 2021/2/1
 */
public enum BillType {

    CHECK_IN("CHECK_IN","入住账单"),
    MONTHLY("UNSETTLED", "月度账单");

    @EnumValue
    private final String value;

    private final String status;

    BillType(String value, String status) {
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
