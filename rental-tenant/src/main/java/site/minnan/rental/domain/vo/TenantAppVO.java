package site.minnan.rental.domain.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Tenant;

/**
 * 小程序上的租客信息
 * @author Minnan on 2021/1/26
 */
@Data
@Builder
public class TenantAppVO {

    private Integer id;

    private String name;

    private String room;

    private String phone;

    public static TenantAppVO assemble(Tenant tenant){
        return TenantAppVO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .room(StrUtil.format("{}-{}", tenant.getHouseName(), tenant.getRoomNumber()))
                .phone(tenant.getPhone())
                .build();
    }

}
