package site.minnan.rental.application.provider;

import cn.hutool.json.JSONArray;

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
     * 根据房间id获取房客名称
     *
     * @param id
     * @return
     */
    List<String> getTenantNameByRoomId(Integer id);

    /**
     * 批量根据房间id获取房客名称
     *
     * @param ids
     * @return
     */
    Map<Integer, List<String>> getTenantNameByRoomIds(Collection<Integer> ids);
}
