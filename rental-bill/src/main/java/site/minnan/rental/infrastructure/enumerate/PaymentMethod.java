package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 支付方式
 *
 * @author Minnan on 2021/1/20
 */
public enum PaymentMethod {

    WEIXIN("WEIXIN", "微信支付"),
    CASH("CASH", "现金支付");

    @EnumValue
    private final String value;

    private final String method;

    PaymentMethod(String value, String method) {
        this.value = value;
        this.method = method;
    }

    public String getValue() {
        return value;
    }

    public String getMethod() {
        return method;
    }
}
