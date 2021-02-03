package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 *
 * @author Minnan on 2021/2/1
 */
public enum BillType {

    CHECK_IN("CHECK_IN","入住账单"),
    MONTHLY("MONTHLY", "月度账单");

    @EnumValue
    private final String value;

    private final String type;

    BillType(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
