package site.minnan.rental.application.provider;

import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.mapper.UtilityMapper;
import site.minnan.rental.domain.vo.SettleQueryVO;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.SettleQueryDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(timeout = 5000, interfaceClass = RoomProviderService.class)
public class UtilityProviderServiceImpl implements UtilityProviderService {

    @Autowired
    private UtilityMapper utilityMapper;


    /**
     * 查询水电
     *
     * @param dtoList
     * @return
     */
    @Override
    public Map<Integer, SettleQueryVO> getUtility(List<SettleQueryDTO> dtoList) {
        //结算时使用的水电单
        QueryWrapper<Utility> endQueryWrapper = new QueryWrapper<>();
        List<Integer> roomIds = dtoList.stream().map(SettleQueryDTO::getRoomId).collect(Collectors.toList());
        endQueryWrapper.select("id", "room_id", "water", "electricity", "create_time")
                .eq("status", UtilityStatus.RECORDING)
                .in("room_id", roomIds);
        List<Utility> endUtilityList = utilityMapper.selectList(endQueryWrapper);
        //账单开始时使用的水电单
        QueryWrapper<Utility> startQueryWrapper = new QueryWrapper<>();
        List<Integer> utilityIds = dtoList.stream().map(SettleQueryDTO::getStartUtilityId).collect(Collectors.toList());
        startQueryWrapper.select("id", "room_id", "water", "electricity", "create_time")
                .in("id", utilityIds);
        List<Utility> startUtilityList = utilityMapper.selectList(startQueryWrapper);
        return Stream.concat(endUtilityList.stream(), startUtilityList.stream())
                .collect(Collectors.groupingBy(Utility::getRoomId, Collectors.collectingAndThen(Collectors.toList(),
                        (e -> {
                            e.sort(Comparator.comparing(Utility::getCreateTime));
                            Utility start = e.get(0);
                            Utility end = e.get(1);
                            return new SettleQueryVO(new JSONObject(start), new JSONObject(end));
                        }))));

    }

    /**
     * 获取房间当前水电度数记录id
     *
     * @param roomId 房间id
     * @return 当前水电度数记录id
     */
    @Override
    public Integer getCurrentUtility(Integer roomId) {
        QueryWrapper<Utility> queryWrapper = new QueryWrapper<>();;
        queryWrapper.select("id").eq("room_id", roomId).eq("status", UtilityStatus.RECORDING);
        Utility utility = utilityMapper.selectOne(queryWrapper);
        return utility.getId();
    }
}
