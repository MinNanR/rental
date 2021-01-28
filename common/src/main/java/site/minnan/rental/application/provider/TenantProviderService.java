package site.minnan.rental.application.provider;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 房客服务
 *
 * @author Minnan on 2021/1/18
 */
public interface TenantProviderService {

    /**
     * 根据房间id获取房客信息
     * 房客名称及其联系电话
     *
     * @param id
     * @return
     */
    JSONArray getTenantInfoByRoomId(Integer id);

    /**
     * 批量根据房客id获取房客信息
     * 所有的房客名称，第一个房客的电话号码
     *
     * @param ids
     * @return
     */
    Map<Integer, JSONObject> getTenantInfoByTenantIds(Collection<Integer> ids);

    Map<Integer, List<Integer>> getTenantIdByRoomId(Collection<Integer> ids);

    /**
     * 根据房客id获取信息
     *
     * @param ids
     * @return
     */
    JSONArray getTenantByIds(List<Integer> ids);
}
