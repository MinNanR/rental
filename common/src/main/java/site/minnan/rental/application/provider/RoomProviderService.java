package site.minnan.rental.application.provider;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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
     * @return 房间原始数据
     */
    ResponseEntity<JSONObject> updateRoomStatus(UpdateRoomStatusDTO dto);

    /**
     * 批量更新房间状态
     *
     * @param dtoList
     * @return 房间原始状态
     */
    ResponseEntity<JSONArray> updateRoomStatusBatch(List<UpdateRoomStatusDTO> dtoList);

    /**
     * 获取房间信息
     *
     * @param id
     * @return
     */
    ResponseEntity<JSONObject> getRoomInfo(Integer id);
}
