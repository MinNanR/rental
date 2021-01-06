package site.minnan.rental.application.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.mapper.UtilityMapper;
import site.minnan.rental.userinterface.dto.GetRecordedRoomDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service(timeout = 5000, interfaceClass = UtilityProviderService.class)
@Slf4j
public class UtilityProviderServiceImpl implements UtilityProviderService{

    @Autowired
    private UtilityMapper utilityMapper;

    /**
     * 获取已经记录了当月水电的房间id
     *
     * @param dto
     * @return
     */
    @Override
    public List<Integer> getRecordedRoom(GetRecordedRoomDTO dto) {
        QueryWrapper<Utility> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", dto.getHouseId())
                .eq("year", dto.getYear())
                .eq("month", dto.getMonth())
                .eq("floor", dto.getFloor());
        return utilityMapper.selectList(wrapper).stream().map(Utility::getRoomId).collect(Collectors.toList());
    }
}
