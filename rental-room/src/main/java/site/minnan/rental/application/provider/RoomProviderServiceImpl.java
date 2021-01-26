package site.minnan.rental.application.provider;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.domain.aggregate.Room;
import site.minnan.rental.domain.mapper.RoomMapper;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.userinterface.dto.UpdateRoomStatusDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service(timeout = 30000, interfaceClass = RoomProviderService.class)
@Slf4j
public class RoomProviderServiceImpl implements RoomProviderService {

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 更新房间状态
     *
     * @param dto
     * @return
     */
    @Override
    public JSONObject updateRoomStatus(UpdateRoomStatusDTO dto) {
        try {
            Room roomOriginal = roomMapper.selectById(dto.getId());
            RoomStatus status = RoomStatus.valueOf(dto.getStatus());
            UpdateWrapper<Room> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", dto.getId())
                    .set("status", status.getValue())
                    .set("update_user_id", dto.getUserId())
                    .set("update_user_name", dto.getUserName())
                    .set("update_time", new Timestamp(System.currentTimeMillis()));
            roomMapper.update(null, wrapper);
            return new JSONObject(roomOriginal);
        } catch (IllegalArgumentException e) {
            log.error("非法参数", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updateRoomStatusBatch(List<UpdateRoomStatusDTO> dtoList) {
        try {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            List<Integer> roomIdList = dtoList.stream().map(UpdateRoomStatusDTO::getId).collect(Collectors.toList());
            QueryWrapper<Room> wrapper = new QueryWrapper<>();
            wrapper.in("id", roomIdList);
            List<Room> roomOriginal = roomMapper.selectList(wrapper);
            List<Room> roomList = dtoList.stream()
                    .map(e -> Room.builder()
                            .id(e.getId())
                            .status(RoomStatus.valueOf(e.getStatus()))
                            .updateUserId(e.getUserId())
                            .updateUserName(e.getUserName())
                            .updateTime(currentTime).build())
                    .collect(Collectors.toList());
            roomMapper.updateRoomStatusBatch(roomList);
            ResponseEntity.success(new JSONArray(roomOriginal));
        } catch (IllegalArgumentException e) {
            log.error("非法参数", e);
            throw e;
        }
    }

    /**
     * 获取房间信息
     *
     * @param id
     * @return
     */
    @Override
    public JSONObject getRoomInfo(Integer id) {
        Room room = roomMapper.selectById(id);
        return new JSONObject(room);
    }

    /**
     * 批量获取房间信息
     *
     * @param ids
     * @return
     */
    @Override
    public JSONArray getRoomInfoBatch(Collection<Integer> ids) {
        List<Room> rooms = roomMapper.selectBatchIds(ids);
        return new JSONArray(rooms);
    }
}
