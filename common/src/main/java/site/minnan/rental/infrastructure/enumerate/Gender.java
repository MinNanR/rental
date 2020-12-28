package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 性别枚举
 *
 * @author Minnan on 2020/12/28
 */
public enum Gender {

    MALE(1, "男"), FEMALE(2, "女");

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String gender;

    Gender(Integer value, String gender) {
        this.value = value;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    /**
     * 枚举数据库存储值
     */
    public Integer getValue() {
        return value;
    }
}
