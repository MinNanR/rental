package site.minnan.rental.application.provider;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.domain.mapper.TenantMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(timeout = 5000, interfaceClass = TenantProviderService.class)
public class TenantProviderServiceImpl implements TenantProviderService {

    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 根据房间id获取房客名称
     *
     * @param id
     * @return
     */
    @Override
    public JSONArray getTenantInfoByRoomId(Integer id) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "phone")
                .eq("room_id", id);
        List<Tenant> tenants = tenantMapper.selectList(queryWrapper);
        return tenants.stream().map(JSONObject::new).collect(JSONArray::new, JSONArray::add, JSONArray::addAll);
    }

    /**
     * 批量根据房间id获取房客名称
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Integer, JSONObject> getTenantInfoByRoomIds(Collection<Integer> ids) {
        List<Tenant> tenantList = tenantMapper.getTenantByRoomIds(ids);
        return tenantList.stream().collect(Collectors.groupingBy(Tenant::getRoomId,
                Collectors.collectingAndThen(Collectors.toList(), e -> {
                    String nameStr = e.stream().map(Tenant::getName).collect(Collectors.joining("、"));
                    String phone = e.stream().findFirst().map(Tenant::getPhone).orElse("");
                    JSONObject jsonObject = new JSONObject();
                    return jsonObject.putOpt("name", nameStr).putOpt("phone", phone);
                })));
    }
}
