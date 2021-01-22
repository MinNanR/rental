package site.minnan.rental.application.provider;

import cn.hutool.json.JSONObject;
import site.minnan.rental.domain.vo.SettleQueryVO;
import site.minnan.rental.userinterface.dto.SettleQueryDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 水电服务
 *
 * @author Minnan on 2021/1/22
 */
public interface UtilityProviderService {

    /**
     * 查询水电
     *
     * @param dtoList
     * @return key:房间id，value：开始的水电情况和结束的水电情况
     */
    Map<Integer, SettleQueryVO> getUtility(List<SettleQueryDTO> dtoList);

    /**
     * 获取房间当前水电度数记录id
     *
     * @param roomId  房间id
     * @return 当前水电度数记录id
     */
    Integer getCurrentUtility(Integer roomId);
}
