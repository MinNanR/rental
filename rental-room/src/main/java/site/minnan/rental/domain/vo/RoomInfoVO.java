package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggregate.Room;

/**
 * @author Minnan on 2020/12/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoVO {

    private Integer id;

    private Integer houseId;

    private String houseName;

    private String roomNumber;

    private Integer floor;

    private Integer price;

    private String status;

    private String statusCode;

    public static RoomInfoVO assemble(Room room){
        return RoomInfoVO.builder()
                .id(room.getId())
                .houseId(room.getHouseId())
                .houseName(room.getHouseName())
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .price(room.getPrice())
                .status(room.getStatus().getStatus())
                .statusCode(room.getStatus().getValue())
                .build();
    }
}
