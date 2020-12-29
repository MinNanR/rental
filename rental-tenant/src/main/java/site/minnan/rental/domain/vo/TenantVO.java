package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggregate.Tenant;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TenantVO {

    private Integer id;

    private String name;

    private String phone;

    private String hometown;

    private String updateUserName;

    private String updateTime;

    public static TenantVO assemble(Tenant tenant) {
        StringBuilder hometown = new StringBuilder();
        Optional.ofNullable(tenant.getHometownProvince()).ifPresent(hometown::append);
        Optional.ofNullable(tenant.getHometownCity()).ifPresent(hometown::append);
        return TenantVO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .phone(tenant.getPhone())
                .hometown(hometown.toString())
                .updateUserName(tenant.getUpdateUserName())
                .updateTime(tenant.getUpdateUserName())
                .build();
    }
}
