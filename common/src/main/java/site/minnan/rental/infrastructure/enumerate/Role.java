package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户类型枚举
 * created by Minnan on 2020/12/17
 */
public enum Role {

    ADMIN("ADMIN", "管理员"),
    LANDLORD("LANDLORD", "房东"),
    TENANT("TENANT", "房客");

    @EnumValue
    private final String value;

    @JsonValue
    private final String roleName;

    Role(String value, String roleName) {
        this.value = value;
        this.roleName = roleName;
    }

    public String getValue(){
        return value;
    }

    public String getRoleName(){
        return roleName;
    }
}
