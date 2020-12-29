package site.minnan.rental.application.provider;

import site.minnan.rental.userinterface.dto.AddTenantUserDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

public interface UserProviderService {

    /**
     * 创建租客用户
     * @param dto 参数
     * @return
     */
    ResponseEntity<?> createTenantUser(AddTenantUserDTO dto);
}
