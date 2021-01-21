package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.*;
import site.minnan.rental.userinterface.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    /**
     * 检查房间号码是否已被使用
     *
     * @param dto
     */
    Boolean checkRoomNumberUsed(CheckRoomNumberDTO dto);

    /**
     * 获取房间下拉框
     *
     * @param dto
     */
    List<RoomDropDown> getRoomDropDown(GetRoomDropDownDTO dto);

    /**
     * 获取所有房间列表
     *
     * @param dto
     * @return 房间列表，按楼层归并
     */
    Collection<FloorVO> getAllRoom(GetFloorDTO dto);
}
