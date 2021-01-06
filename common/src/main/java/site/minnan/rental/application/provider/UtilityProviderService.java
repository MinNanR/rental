package site.minnan.rental.application.provider;

import site.minnan.rental.userinterface.dto.GetRecordedRoomDTO;

import java.util.List;

/**
 * 水电记录服务
 */
public interface UtilityProviderService {

    /**
     * 获取已经记录了当月水电的房间id
     *
     * @param dto
     * @return
     */
    List<Integer> getRecordedRoom(GetRecordedRoomDTO dto);
}
