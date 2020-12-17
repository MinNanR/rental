package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.HouseService;
import site.minnan.rental.domain.aggretes.AuthUser;
import site.minnan.rental.domain.aggretes.House;
import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.domain.mapper.HouseMapper;
import site.minnan.rental.domain.vo.HouseVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddHouseDTO;
import site.minnan.rental.userinterface.dto.GetHouseListDTO;

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
                .address(dto.getAddress())
                .directorName(dto.getDirectorName())
                .directorPhone(dto.getDirectorPhone())
                .build();
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newHouse.setCreateUser(jwtUser);
        houseMapper.insert(newHouse);
    }
}
