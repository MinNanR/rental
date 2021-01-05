package site.minnan.rental.application.provider;

import site.minnan.rental.userinterface.dto.UpdateRoomStatusDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.util.List;

/**
 * 房间服务
 *
 * @author Minnan on 2020/12/29
 */
public interface RoomProviderService {

    /**
     * 更新房间状态
     *
     * @param dto
     * @return
     */
    ResponseEntity<?> updateRoomStatus(UpdateRoomStatusDTO dto);

    ResponseEntity<?> updateRoomStatusBatch(List<UpdateRoomStatusDTO> dtoList);
}
