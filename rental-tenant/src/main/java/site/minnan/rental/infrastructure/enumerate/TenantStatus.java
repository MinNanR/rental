package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import site.minnan.rental.domain.aggregate.Tenant;

/**
 * 房客状态
 *
 * @author Minnan on 2020/12/29
 */
public enum TenantStatus {

    LIVING("LIVING", "在租"),
    LEFT("LEFT", "已退租"),
    DELETED("DELETED", "已删除");

    @EnumValue
    private final String value;

    private final String status;

    TenantStatus(String value, String status) {
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
