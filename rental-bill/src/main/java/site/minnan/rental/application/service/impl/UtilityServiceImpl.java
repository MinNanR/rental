package site.minnan.rental.application.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.application.service.UtilityService;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.entity.UtilityRecord;
import site.minnan.rental.domain.mapper.UtilityMapper;
import site.minnan.rental.domain.mapper.UtilityRecordMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityRecordVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.infrastructure.exception.UnmodifiableException;
import site.minnan.rental.infrastructure.utils.RedisUtil;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;
import site.minnan.rental.userinterface.dto.GetRecordListDTO;
import site.minnan.rental.userinterface.dto.GetUtilityDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtilityServiceImpl implements UtilityService {

    @Autowired
    private UtilityMapper utilityMapper;

    @Autowired
    private UtilityRecordMapper utilityRecordMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public void addUtilityBatch(List<AddUtilityDTO> dtoList) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        beforeRecord(dtoList.get(0));
        Timestamp current = new Timestamp(System.currentTimeMillis());
        List<Utility> newUtilityList = dtoList.stream()
                .map(Utility::assemble)
                .peek(e -> {
                    e.setCreateUser(jwtUser, current);
                })
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
     * 登记水电（单个房间）
     *
     * @param dto
     */
    @Override
    @Transactional
    public void addUtility(AddUtilityDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        beforeRecord(dto);
        Utility utility = Utility.assemble(dto);
        utility.setCreateUser(jwtUser);
        UpdateWrapper<Utility> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("room_id", dto.getRoomId())
                .eq("status", UtilityStatus.RECORDING)
                .set("status", UtilityStatus.RECORDED);
        utilityMapper.update(null, updateWrapper);
        utilityMapper.insert(utility);
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
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> queryWrapper.eq("house_id", s));
        Optional.ofNullable(dto.getRoomNumber())
                .map(String::trim)
                .ifPresent(s -> queryWrapper.likeRight("room_number", s));
        Optional.ofNullable(dto.getYear()).ifPresent(s -> queryWrapper.eq("year(create_time)", s));
        Optional.ofNullable(dto.getMonth()).ifPresent(s -> queryWrapper.eq("month(create_time)", s));
        Optional.ofNullable(dto.getDate()).ifPresent(s -> queryWrapper.eq("datediff(create_time, '" + s + "')", 0));
        Optional.ofNullable(dto.getRoomId()).ifPresent(s -> queryWrapper.eq("room_id", s));
        queryWrapper.orderByDesc("create_time", "room_number");
        IPage<Utility> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Utility> page = utilityMapper.selectPage(queryPage, queryWrapper);
        List<UtilityVO> vo = page.getRecords().stream().map(UtilityVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(vo, page.getTotal());
    }

    /**
     * 查询登记水电的记录
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<UtilityRecordVO> getRecordList(GetRecordListDTO dto) {
        QueryWrapper<UtilityRecord> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getHouseId()).ifPresent(s -> queryWrapper.eq("house_id", s));
        queryWrapper.orderByDesc("record_date");
        Page<UtilityRecord> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<UtilityRecord> page = utilityRecordMapper.selectPage(queryPage, queryWrapper);
        List<UtilityRecordVO> list =
                page.getRecords().stream().map(UtilityRecordVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(list, page.getTotal());
    }

    private void beforeRecord(AddUtilityDTO first) {
        //检查今天是否有记录
        Object haveRecord = redisUtil.getValue("haveRecord_" + first.getHouseId());
        if (haveRecord == null) {
            DateTime now = DateTime.now();
            DateTime endOfDay = DateUtil.endOfDay(now);
            long timeout = DateUtil.between(now, endOfDay, DateUnit.SECOND);
            String recordName = StrUtil.format("{}{}记录", first.getHouseName(), now.toString("yyyy年M月d日"));
            UtilityRecord record = new UtilityRecord(first.getHouseId(), first.getHouseName(), recordName, now);
            utilityRecordMapper.insert(record);
            redisUtil.valueSet("haveRecord_" + first.getHouseId(), true, Duration.ofSeconds(timeout));
        }
    }
}
