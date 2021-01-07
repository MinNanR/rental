package site.minnan.rental.application.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.provider.BillProviderService;
import site.minnan.rental.application.service.RoomService;
import site.minnan.rental.domain.aggregate.Room;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.RoomMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.RoomDropDown;
import site.minnan.rental.domain.vo.RoomInfoVO;
import site.minnan.rental.domain.vo.RoomVO;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.userinterface.dto.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RooServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 创建房间参数
     *
     * @param dto
     */
    @Override
    public void addRoom(AddRoomDTO dto) {
        Room room = Room.builder()
                .houseId(dto.getHouseId())
                .houseName(dto.getHouseName())
                .roomNumber(dto.getRoomNumber())
                .floor(dto.getFloor())
                .price(dto.getPrice())
                .status(RoomStatus.VACANT)
                .build();
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        room.setCreateUser(jwtUser);
        roomMapper.insert(room);
    }

    /**
     * 查询房间列表
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<RoomVO> getRoomList(GetRoomListDTO dto) {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", dto.getHouseId());
        Optional.ofNullable(dto.getFloor()).ifPresent(s -> wrapper.eq("floor", s));
        Optional.ofNullable(dto.getStatus()).ifPresent(s -> wrapper.eq("status", s));
        wrapper.ne("status", RoomStatus.DELETED.getValue())
                .orderByDesc("update_time");
        Page<Room> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Room> page = roomMapper.selectPage(queryPage, wrapper);
        List<RoomVO> vo = page.getRecords().stream().map(RoomVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(vo, page.getTotal());
    }

    /**
     * 获取房屋详情
     *
     * @param dto
     * @return
     */
    @Override
    public RoomInfoVO getRoomInfo(DetailsQueryDTO dto) {
        Room room = roomMapper.selectById(dto.getId());
        if (room == null) {
            throw new EntityNotExistException("房间不存在");
        }
        return RoomInfoVO.assemble(room);
    }

    /**
     * 更新房间
     *
     * @param dto
     */
    @Override
    public void updateRoom(UpdateRoomDTO dto) {
        Integer check = roomMapper.checkRoomExists(dto.getId());
        if (check == null) {
            throw new EntityNotExistException("房间不存在");
        }
        UpdateWrapper<Room> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", dto.getId());
        Optional.ofNullable(dto.getRoomNumber()).ifPresent(s -> wrapper.set("room_number", s));
        Optional.ofNullable(dto.getFloor()).ifPresent(s -> wrapper.set("floor", s));
        Optional.ofNullable(dto.getPrice()).ifPresent(s -> wrapper.set("price", s));
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wrapper.set("update_user_id", jwtUser.getId())
                .set("update_user_name", jwtUser.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()));
        roomMapper.update(null, wrapper);
    }

    /**
     * 检查房间号码是否已被使用
     *
     * @param dto
     */
    @Override
    public Boolean checkRoomNumberUsed(CheckRoomNumberDTO dto) {
        Integer check = roomMapper.checkRoomNumberUsed(dto.getHouseId(), dto.getRoomNumber());
        if (dto.getId() == null) {
            return check != null;
        } else {
            return check != null && !check.equals(dto.getId());
        }
    }

    /**
     * 获取房间下拉框
     *
     * @param dto
     */
    @Override
    public List<RoomDropDown> getRoomDropDown(GetRoomDropDownDTO dto) {
        return roomMapper.getRoomDropDown(dto.getHouseId());
    }

    /**
     * 获取楼层下拉框
     *
     * @param dto
     * @return
     */
    @Override
    public List<Integer> getFloorDropDown(GetFloorDropDownDTO dto) {
        return roomMapper.getFloorDropDown(dto.getHouseId());
    }
}
