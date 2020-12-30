package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.HouseService;
import site.minnan.rental.domain.aggregate.House;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.HouseMapper;
import site.minnan.rental.domain.vo.HouseDropDown;
import site.minnan.rental.domain.vo.HouseInfoVO;
import site.minnan.rental.domain.vo.HouseVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.userinterface.dto.AddHouseDTO;
import site.minnan.rental.userinterface.dto.DetailsQueryDTO;
import site.minnan.rental.userinterface.dto.GetHouseListDTO;
import site.minnan.rental.userinterface.dto.UpdateHouseDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    /**
     * 获取房屋列表
     *
     * @param dto 查询参数
     * @return
     */
    @Override
    public ListQueryVO<HouseVO> getHouseList(GetHouseListDTO dto) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getAddress()).ifPresent(s -> wrapper.like("address", s));
        wrapper.orderByDesc("update_time");
        Page<House> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<House> houseList = houseMapper.selectPage(page, wrapper);
        List<HouseVO> voList = houseList.getRecords().stream().map(HouseVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(voList, houseList.getTotal());
    }

    /**
     * 添加房屋
     *
     * @param dto 添加房屋参数
     */
    @Override
    public void addHouse(AddHouseDTO dto) {
        House newHouse = House.builder()
                .houseName(dto.getHouseName())
                .address(dto.getAddress())
                .directorName(dto.getDirectorName())
                .directorPhone(dto.getDirectorPhone())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .build();
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newHouse.setCreateUser(jwtUser);
        houseMapper.insert(newHouse);
    }

    /**
     * 获取房屋详情
     *
     * @param dto 查询参数
     * @return
     */
    @Override
    public HouseInfoVO getHouseInfo(DetailsQueryDTO dto) {
        House house = houseMapper.selectById(dto.getId());
        if (house == null) {
            throw new EntityNotExistException("房屋不存在");
        }
        return HouseInfoVO.assemble(house);
    }

    /**
     * 更新房屋信息
     *
     * @param dto 更新参数
     */
    @Override
    public void updateHouse(UpdateHouseDTO dto) {
        House house = houseMapper.selectById(dto.getId());
        if (house == null) {
            throw new EntityNotExistException("房屋不存在");
        }
        UpdateWrapper<House> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", dto.getId());
        Optional.ofNullable(dto.getAddress()).ifPresent(s -> wrapper.set("address", s));
        Optional.ofNullable(dto.getHouseName()).ifPresent(s -> wrapper.set("house_name", s));
        Optional.ofNullable(dto.getDirectorName()).ifPresent(s -> wrapper.set("director_name", s));
        Optional.ofNullable(dto.getDirectorPhone()).ifPresent(s -> wrapper.set("director_phone", s));
        Optional.ofNullable(dto.getLatitude()).ifPresent(s -> wrapper.set("latitude", s));
        Optional.ofNullable(dto.getLongitude()).ifPresent(s -> wrapper.set("longitude", s));
        wrapper.set("update_time", new Timestamp(System.currentTimeMillis()));
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wrapper.set("update_user_id", jwtUser.getId());
        wrapper.set("update_user_name", jwtUser.getRealName());
        int i = houseMapper.update(null, wrapper);
    }

    @Override
    public List<HouseDropDown> getHouseDropDown() {
        List<House> houseList = houseMapper.getHouseDropDown();
        return houseList.stream().map(e -> new HouseDropDown(e.getId(), e.getHouseName())).collect(Collectors.toList());
    }
}
