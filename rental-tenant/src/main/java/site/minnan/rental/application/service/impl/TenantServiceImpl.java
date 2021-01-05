package site.minnan.rental.application.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.application.provider.RoomProviderService;
import site.minnan.rental.application.provider.UserProviderService;
import site.minnan.rental.application.service.TenantService;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.TenantMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.TenantDropDownVO;
import site.minnan.rental.domain.vo.TenantInfoVO;
import site.minnan.rental.domain.vo.TenantVO;
import site.minnan.rental.infrastructure.enumerate.Gender;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.infrastructure.enumerate.TenantStatus;
import site.minnan.rental.infrastructure.exception.EntityAlreadyExistException;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.userinterface.dto.*;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Reference
    private UserProviderService userProviderService;

    @Reference
    private RoomProviderService roomProviderService;

    /**
     * 添加房客
     *
     * @param dto
     */
    @Override
    @Transactional
    public void addTenant(AddTenantDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer check = tenantMapper.checkTenantExistByIdentificationNumber(dto.getIdentificationNumber());
        if (check != null) {
            throw new EntityAlreadyExistException("房客已存在");
        }
        Tenant tenant = Tenant.builder()
                .name(dto.getName())
                .gender(Gender.valueOf(dto.getGender()))
                .birthday(dto.getBirthday())
                .phone(dto.getPhone())
                .identificationNumber(dto.getIdentificationNumber())
                .hometownProvince(dto.getHometownProvince())
                .hometownCity(dto.getHometownCity())
                .houseId(dto.getHouseId())
                .houseName(dto.getHouseName())
                .roomId(dto.getRoomId())
                .roomNumber(dto.getRoomNumber())
                .status(TenantStatus.LIVING)
                .build();
        tenant.setCreateUser(jwtUser);
        tenantMapper.insert(tenant);
        UpdateRoomStatusDTO updateRoomStatusDTO = UpdateRoomStatusDTO.builder()
                .id(dto.getRoomId())
                .status(RoomStatus.ON_RENT.getValue())
                .build();
        roomProviderService.updateRoomStatus(updateRoomStatusDTO);
        AddTenantUserDTO tenantUserDTO = AddTenantUserDTO.builder()
                .phone(dto.getPhone())
                .realName(dto.getName())
                .userId(jwtUser.getId())
                .userName(jwtUser.getRealName())
                .build();
        userProviderService.createTenantUser(tenantUserDTO);
    }

    /**
     * 列表查询房客
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<TenantVO> getTenantList(GetTenantListDTO dto) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getName()).ifPresent(s -> wrapper.like("name", s));
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> wrapper.eq("house_id", s));
        Optional.ofNullable(dto.getRoomNumber()).ifPresent(s -> wrapper.like("room_number", s));
        Optional.ofNullable(dto.getRoomId()).ifPresent(s -> wrapper.eq("room_id", s));
        Optional.ofNullable(dto.getHometownProvince()).ifPresent(s -> wrapper.eq("hometown_province", s));
        Optional.ofNullable(dto.getHometownCity()).ifPresent(s -> wrapper.eq("hometown_city", s));
        wrapper.ne("status", TenantStatus.DELETED.getValue());
        wrapper.orderByDesc("update_time");
        Page<Tenant> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Tenant> page = tenantMapper.selectPage(queryPage, wrapper);
        List<TenantVO> voList = page.getRecords().stream().map(TenantVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(voList, page.getTotal());
    }

    /**
     * 查询该房间所住的房客
     *
     * @param dto
     * @return
     */
    @Override
    public List<TenantVO> getTenantByRoom(GetTenantByRoomDTO dto) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id", dto.getRoomId())
                .eq("status", TenantStatus.LIVING);
        List<Tenant> tenantList = tenantMapper.selectList(wrapper);
        return tenantList.stream().map(TenantVO::assemble).collect(Collectors.toList());
    }

    /**
     * 查询房客详情
     *
     * @param dto
     * @return
     */
    @Override
    public TenantInfoVO getTenantInfo(DetailsQueryDTO dto) {
        Tenant tenant = tenantMapper.selectById(dto.getId());
        if (tenant == null) {
            throw new EntityNotExistException("房客不存在");
        }
        return TenantInfoVO.assemble(tenant);
    }

    /**
     * 获取房客下拉框
     *
     * @param dto
     * @return
     */
    @Override
    public List<TenantDropDownVO> getTenantDropDown(GetTenantDropDownDTO dto) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        wrapper.like("name", dto.getName());
        wrapper.ne("room_id", dto.getRoomId());
        wrapper.or().eq("status", TenantStatus.LEFT);
        List<Tenant> tenantList = tenantMapper.selectList(wrapper);
        return tenantList.stream().map(TenantDropDownVO::assemble).collect(Collectors.toList());
    }

    /**
     * 房客迁移房间
     *
     * @param dto
     */
    @Override
    public void tenantMove(TenantMoveDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> tenantIdList = dto.getTenantIdList();
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", tenantIdList);
        Set<Integer> roomIdList =
                tenantMapper.selectList(queryWrapper).stream().map(Tenant::getRoomId).collect(Collectors.toSet());
        UpdateWrapper<Tenant> updateWrapper = new UpdateWrapper<>();

        updateWrapper.set("house_id", dto.getHouseId())
                .set("house_name", dto.getHouseName())
                .set("room_id", dto.getRoomId())
                .set("room_number", dto.getRoomNumber())
                .set("status", TenantStatus.LIVING)
                .set("update_user_id", jwtUser.getId())
                .set("update_user_name", jwtUser.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()))
                .in("id", tenantIdList);
        tenantMapper.update(null, updateWrapper);
        List<UpdateRoomStatusDTO> updateRoomStatusDTOList = new ArrayList<>();
        for (Integer id : roomIdList) {
            Integer check = tenantMapper.checkRoomOnRentByRoomId(id);
            if (check == null) {
                UpdateRoomStatusDTO roomStatusDTO =
                        UpdateRoomStatusDTO.builder()
                                .id(id)
                                .status(RoomStatus.VACANT.getValue())
                                .userId(jwtUser.getId())
                                .userName(jwtUser.getRealName())
                                .build();
                updateRoomStatusDTOList.add(roomStatusDTO);
            }
        }
        UpdateRoomStatusDTO roomStatusDTO =
                UpdateRoomStatusDTO.builder()
                        .id(dto.getRoomId())
                        .status(RoomStatus.ON_RENT.getValue())
                        .userId(jwtUser.getId())
                        .userName(jwtUser.getRealName())
                        .build();
        updateRoomStatusDTOList.add(roomStatusDTO);
        roomProviderService.updateRoomStatusBatch(updateRoomStatusDTOList);
    }

    /**
     * 修改房客信息
     *
     * @param dto
     */
    @Override
    public void updateTenant(UpdateTenantDTO dto) {
        Tenant tenant = tenantMapper.selectById(dto.getId());
        if (tenant == null) {
            throw new EntityNotExistException("房客不存在");
        }
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateWrapper<Tenant> wrapper = new UpdateWrapper<>();
        Optional.ofNullable(dto.getName()).ifPresent(s -> wrapper.set("name", s));
        Optional.ofNullable(dto.getGender()).ifPresent(s -> wrapper.set("gender", s));
        Optional.ofNullable(dto.getPhone()).ifPresent(s -> wrapper.set("phone", s));
        Optional.ofNullable(dto.getIdentificationNumber()).ifPresent(s -> wrapper.set("identification_number", s));
        Optional.ofNullable(dto.getBirthday()).ifPresent(s -> wrapper.set("birthday", s));
        Optional.ofNullable(dto.getHometownProvince()).ifPresent(s -> wrapper.set("hometown_province", s));
        Optional.ofNullable(dto.getHometownCity()).ifPresent(s -> wrapper.set("hometown_city", s));
        wrapper.set("update_user_id", user.getId());
        wrapper.set("update_user_name", user.getRealName());
        wrapper.set("update_time", new Timestamp(System.currentTimeMillis()));
        wrapper.eq("id", dto.getId());
        tenantMapper.update(null, wrapper);
    }

    /**
     * 房客退租
     *
     * @param dto
     */
    @Override
    public void surrender(SurrenderDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tenant tenant = tenantMapper.selectById(dto.getId());
        if (tenant == null) {
            throw new EntityNotExistException("房客不存在");
        }
        UpdateWrapper<Tenant> wrapper = new UpdateWrapper<>();
        wrapper.set("status", TenantStatus.LEFT)
                .set("update_user_id", jwtUser.getId())
                .set("update_user_name", jwtUser.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()))
                .eq("id", dto.getId());
        tenantMapper.update(null ,wrapper);
        Integer check = tenantMapper.checkRoomOnRentByRoomId(tenant.getRoomId());
        if (check == null) {
            UpdateRoomStatusDTO updateRoomStatusDTO = UpdateRoomStatusDTO.builder()
                    .id(tenant.getId())
                    .status(RoomStatus.VACANT.getValue())
                    .userId(jwtUser.getId())
                    .userName(jwtUser.getRealName())
                    .build();
            roomProviderService.updateRoomStatus(updateRoomStatusDTO);
        }
    }
}
