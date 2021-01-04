package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggregate.Room;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoomVO {

    private Integer id;

    private String  roomNumber;

    private Integer price;

    private String status;

    private String updateUserName;

    private String updateTime;

    public static RoomVO assemble(Room room){
        return RoomVO.builder()
                .id(room.getId())
                .roomNumber(StrUtil.format("{}-{}", room.getHouseName(), room.getRoomNumber()))
                .price(room.getPrice())
                .status(room.getStatus().getStatus())
                .updateUserName(room.getUpdateUserName())
                .updateTime(DateUtil.format(room.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }
}
