package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
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

    private String gender;

    private String phone;

    private String hometown;

    private String identificationNumber;

    private String birthday;

    private String status;

    private String updateUserName;

    private String updateTime;

    public static TenantVO assemble(Tenant tenant) {
        TenantVOBuilder builder = TenantVO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .phone(tenant.getPhone())
                .status(tenant.getStatus().getStatus())
                .updateUserName(tenant.getUpdateUserName())
                .updateTime(DateUtil.format(tenant.getUpdateTime(), "yyyy-MM-dd HH:mm"));
        if(tenant.getIdentificationNumber() != null){
            StringBuilder hometown = new StringBuilder();
            Optional.ofNullable(tenant.getHometownProvince()).ifPresent(hometown::append);
            Optional.ofNullable(tenant.getHometownCity()).ifPresent(hometown::append);
            builder.gender(tenant.getGender().getGender())
                    .hometown(hometown.toString())
                    .identificationNumber(StrUtil.replace(tenant.getIdentificationNumber(), 4, 16, '*'))
                    .birthday(DateUtil.format(tenant.getBirthday(), "yyyy年M月d日"));
        }
        return builder.build();
    }
}
