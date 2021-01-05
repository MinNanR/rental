package site.minnan.rental.application.provider;

import com.alibaba.dubbo.config.annotation.Service;
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
import java.util.List;
import java.util.stream.Collectors;

@Service(timeout = 5000, interfaceClass = RoomProviderService.class)
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
    public ResponseEntity<?> updateRoomStatus(UpdateRoomStatusDTO dto) {
        try {
            RoomStatus status = RoomStatus.valueOf(dto.getStatus());
            UpdateWrapper<Room> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", dto.getId())
                    .set("status", status.getValue())
                    .set("update_user_id", dto.getUserId())
                    .set("update_user_name", dto.getUserName())
                    .set("update_time", new Timestamp(System.currentTimeMillis()));
            roomMapper.update(null, wrapper);
            return ResponseEntity.success();
        } catch (IllegalArgumentException e) {
            log.error("非法参数", e);
            return ResponseEntity.fail(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateRoomStatusBatch(List<UpdateRoomStatusDTO> dtoList) {
        try {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            List<Room> roomList = dtoList.stream()
                    .map(e -> Room.builder()
                            .id(e.getId())
                            .status(RoomStatus.valueOf(e.getStatus()))
                            .updateUserId(e.getUserId())
                            .updateUserName(e.getUserName())
                            .updateTime(currentTime).build())
                    .collect(Collectors.toList());
            roomMapper.updateRoomStatusBatch(roomList);
            return ResponseEntity.success();
        } catch (IllegalArgumentException e) {
            log.error("非法参数", e);
            return ResponseEntity.fail(e.getMessage());
        }
    }
}
