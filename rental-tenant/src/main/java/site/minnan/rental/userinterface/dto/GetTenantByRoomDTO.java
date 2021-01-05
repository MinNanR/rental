package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 查找该房间所住的房客
 * @author Minnan on 2020/01/05
 */
@Data
public class GetTenantByRoomDTO {

    private Integer roomId;
}
