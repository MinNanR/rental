package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.infrastructure.enumerate.Gender;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TenantInfoVO {

    private Integer id;

    private String name;

    private Gender gender;

    private String phone;

    private String birthday;

    private String hometownProvince;

    private String hometownCity;

    private String identificationNumber;

    private Integer houseId;

    private String houseName;

    private Integer roomId;

    private String roomNumber;

    private String status;

    private String statusCode;

    public static TenantInfoVO assemble(Tenant tenant) {
        return TenantInfoVO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .gender(tenant.getGender())
                .phone(tenant.getPhone())
                .birthday(DateUtil.format(tenant.getBirthday(), "yyyy-MM-dd"))
                .hometownProvince(tenant.getHometownProvince())
                .hometownCity(tenant.getHometownCity())
                .identificationNumber(tenant.getIdentificationNumber())
                .houseId(tenant.getHouseId())
                .houseName(tenant.getHouseName())
                .roomId(tenant.getRoomId())
                .roomNumber(tenant.getRoomNumber())
                .status(tenant.getStatus().getStatus())
                .statusCode(tenant.getStatus().getValue())
                .build();
    }
}
