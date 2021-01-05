package site.minnan.rental.userinterface.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DisableTenantUserDTO implements Serializable {

    private Integer userId;

    private Integer updateUserId;

    private String updateUserName;
}
