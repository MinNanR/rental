package site.minnan.rental.domain.vo;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggregate.Tenant;

/**
 * 房客下拉框
 * @author Minnan on 2021/0104
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantDropDownVO {

    private Integer id;

    private String name;

    private String roomNumber;

    public static TenantDropDownVO assemble(Tenant tenant){
        return TenantDropDownVO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .roomNumber(StrUtil.format("{}-{}", tenant.getHouseName(), tenant.getRoomNumber()))
                .build();
    }
}
