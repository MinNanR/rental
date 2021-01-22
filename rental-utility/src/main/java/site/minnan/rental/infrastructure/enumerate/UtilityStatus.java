package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 水电单状态
 *
 * @author Minnan on 2021/1/22
 */
public enum UtilityStatus {

    RECORDING("RECORDING", "使用中"),
    RECORDED("RECORDED", "已归档");

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
