package site.minnan.rental.application.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.domain.mapper.TenantMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Service(timeout = 5000, interfaceClass = TenantProviderService.class)
@Service
public class TenantProviderServiceImpl implements TenantProviderService{

    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 根据房间id获取房客名称
     *
     * @param id
     * @return
     */
    @Override
    public List<String> getTenantNameByRoomId(Integer id) {
        return tenantMapper.getTenantNameByRoomId(id);
    }

    /**
     * 批量根据房间id获取房客名称
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Integer, List<String>> getTenantNameByRoomIds(Collection<Integer> ids) {
        List<Tenant> tenantList = tenantMapper.getTenantByRoomIds(ids);
        return tenantList.stream().collect(Collectors.groupingBy(Tenant::getRoomId,
                Collectors.mapping(Tenant::getName, Collectors.toList())));
    }
}
