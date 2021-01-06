package site.minnan.rental.userinterface.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取已经记录了水电的房间id参数
 * @author Minnan on 2021/01/06
 */
@Data
@Builder
public class GetRecordedRoomDTO implements Serializable {

    private Integer houseId;

    private Integer year;

    private Integer month;

    private Integer floor;

}
