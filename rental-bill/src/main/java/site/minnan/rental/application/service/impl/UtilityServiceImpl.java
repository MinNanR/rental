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
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.userinterface.dto.AddUtilityBatchDTO;
import site.minnan.rental.userinterface.dto.GetUtilityListDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 水电单处理
 *
 * @author Minnan on 2021/01/06
 */
@Service
public class UtilityServiceImpl implements UtilityService {

    @Autowired
    private UtilityMapper utilityMapper;

    @Override
    public void addUtilityBatch(AddUtilityBatchDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Utility> utilityList = dto.getUtilityList().stream()
                .map(e -> Utility.assemble(e, dto))
                .peek(e -> e.setCreateUser(jwtUser))
                .collect(Collectors.toList());
        utilityMapper.insertBatch(utilityList);
    }

    /**
     * 查询水电记录
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<UtilityVO> getUtilityList(GetUtilityListDTO dto) {
        QueryWrapper<Utility> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> wrapper.eq("house_id", s));
        Optional.ofNullable(dto.getRoomNumber()).ifPresent(s -> wrapper.eq("room_number", s));
        Optional.ofNullable(dto.getYear()).ifPresent(s -> wrapper.eq("year", s));
        Optional.ofNullable(dto.getMonth()).ifPresent(s -> wrapper.eq("month", s));
        Optional.ofNullable(dto.getStatus()).ifPresent(s -> wrapper.eq("status", s));
        wrapper.orderByDesc("update_time");
        Page<Utility> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Utility> page = utilityMapper.selectPage(queryPage, wrapper);
        List<UtilityVO> list = page.getRecords().stream().map(UtilityVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(list, page.getTotal());
    }

    /**
     * 更新水电记录
     *
     * @param dto
     */
    @Override
    public void updateUtility(UpdateUtilityDTO dto) {
        Utility check = utilityMapper.selectById(dto.getId());
        if (check == null) {
            throw new EntityNotExistException("记录不存在");
        }
        UpdateWrapper<Utility> wrapper = new UpdateWrapper<>();
        Optional.ofNullable(dto.getWater()).ifPresent(s -> wrapper.set("water", s));
        Optional.ofNullable(dto.getElectricity()).ifPresent(s -> wrapper.set("electricity", s));
        wrapper.eq("id", dto.getId());
        utilityMapper.update(null ,wrapper);
    }
}
