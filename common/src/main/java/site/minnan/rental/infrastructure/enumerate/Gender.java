package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 性别枚举
 *
 * @author Minnan on 2020/12/28
 */
public enum Gender {

    FEMALE("FEMALE", "女"),
    MALE("MALE", "男");

    @EnumValue
    private final String value;

    @JsonValue
    private final String gender;

    Gender(String  value, String gender) {
        this.value = value;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String getValue() {
        return value;
    }
}
