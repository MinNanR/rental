package site.minnan.rental.infrastructure.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 房间状态
 *
 * @author Minnan on 2020/12/29
 */
public enum RoomStatus {

    ON_RENT("ON_RENT", "在租"),
    VACANT("VACANT", "空闲"),
    DELETED("DELETED", "已删除");

    @EnumValue
    private final String value;

    private final String status;

    RoomStatus(String value, String status) {
        this.value = value;
        this.status = status;
    }

    public String getValue(){
        return value;
    }

    public String getStatus(){
        return status;
    }
}
