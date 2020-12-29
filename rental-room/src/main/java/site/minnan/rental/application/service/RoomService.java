package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.RoomVO;
import site.minnan.rental.userinterface.dto.AddRoomDTO;
import site.minnan.rental.userinterface.dto.DetailsQueryDTO;
import site.minnan.rental.userinterface.dto.GetRoomListDTO;
import site.minnan.rental.domain.vo.RoomInfoVO;
import site.minnan.rental.userinterface.dto.UpdateRoomDTO;

/**
 * 房间相关操作
 *
 * @author Minnan on 2020/12/29
 */
public interface RoomService {

    /**
     * 创建房间参数
     *
     * @param dto
     */
    void addRoom(AddRoomDTO dto);

    /**
     * 查询房间列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<RoomVO> getRoomList(GetRoomListDTO dto);

    /**
     * 获取房屋详情
     *
     * @param dto
     * @return
     */
    RoomInfoVO getRoomInfo(DetailsQueryDTO dto);

    /**
     * 更新房间
     *
     * @param dto
     */
    void updateRoom(UpdateRoomDTO dto);
}
