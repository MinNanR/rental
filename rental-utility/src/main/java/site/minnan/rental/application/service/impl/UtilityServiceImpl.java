package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.UtilityService;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.UtilityMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.infrastructure.exception.UnmodifiableException;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;
import site.minnan.rental.userinterface.dto.GetUtilityDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtilityServiceImpl implements UtilityService {

    @Autowired
    private UtilityMapper utilityMapper;

    @Override
    public void addUtilityBatch(List<AddUtilityDTO> dtoList) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        List<Utility> newUtilityList = dtoList.stream()
                .map(Utility::assemble)
                .peek(e -> e.setCreateUser(jwtUser, current))
                .collect(Collectors.toList());
        Set<Integer> roomIdSet = newUtilityList.stream().map(Utility::getRoomId).collect(Collectors.toSet());
        UpdateWrapper<Utility> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("status", UtilityStatus.RECORDING)
                .in("room_id", roomIdSet)
                .set("status", UtilityStatus.RECORDED);
        utilityMapper.update(null, updateWrapper);
        utilityMapper.addUtilityBatch(newUtilityList);
    }

    /**
     * 修改水电记录
     *
     * @param dto
     */
    @Override
    public void updateUtility(UpdateUtilityDTO dto) {
        QueryWrapper<Utility> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", dto.getId())
                .select("id", "status");
        Utility utility = utilityMapper.selectOne(queryWrapper);
        if (!UtilityStatus.RECORDING.equals(utility.getStatus())) {
            throw new UnmodifiableException("当前记录不可修改");
        }
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateWrapper<Utility> updateWrapper = new UpdateWrapper<>();
        Optional.ofNullable(dto.getWater()).ifPresent(s -> updateWrapper.set("water", s));
        Optional.ofNullable(dto.getElectricity()).ifPresent(s -> updateWrapper.set("electricity", s));
        updateWrapper.eq("id", dto.getId())
                .set("update_user_id", jwtUser.getId())
                .set("update_user_name", jwtUser.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()));
        utilityMapper.update(null, updateWrapper);
    }

    /**
     * 获取水电记录
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<UtilityVO> getUtilityList(GetUtilityDTO dto) {
        QueryWrapper<Utility> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> queryWrapper.eq("house_id", dto.getHouseId()));
        Optional.ofNullable(dto.getRoomNumber())
                .map(String::trim)
                .ifPresent(s -> queryWrapper.likeRight("room_number", s));
        Optional.ofNullable(dto.getYear()).ifPresent(s -> queryWrapper.eq("year(create_time)", s));
        Optional.ofNullable(dto.getMonth()).ifPresent(s -> queryWrapper.eq("month(create_time)", s));
        queryWrapper.orderByDesc("create_time");
        IPage<Utility> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Utility> page = utilityMapper.selectPage(queryPage, queryWrapper);
        List<UtilityVO> vo = page.getRecords().stream().map(UtilityVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(vo, page.getTotal());
    }
}
