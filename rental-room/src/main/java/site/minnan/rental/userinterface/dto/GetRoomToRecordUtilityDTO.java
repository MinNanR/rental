package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 获取需要记录水电的房间
 * @author Minnan on 2021/01/06
 */
@Data
public class GetRoomToRecordUtilityDTO {

    private Integer houseId;

    private Integer year;

    private Integer month;

    private Integer floor;
}
