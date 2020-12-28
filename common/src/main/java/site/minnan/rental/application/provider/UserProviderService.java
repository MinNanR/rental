package site.minnan.rental.application.provider;

import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.userinterface.response.ResponseEntity;

public interface UserProviderService {

    /**
     * 创建租客用户
     *
     * @param phone    手机号码
     * @param realName 租客名称
     * @param user     操作者
     * @return
     */
    ResponseEntity<?> createTenantUser(String phone, String realName, JwtUser user);
}
