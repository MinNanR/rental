package site.minnan.rental.application.provider;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.application.service.AuthUserService;
import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.infrastructure.enumerate.Role;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.sql.ResultSet;

@Service(timeout = 5000, interfaceClass = UserProviderService.class)
@Slf4j
public class UserProviderServiceImpl implements UserProviderService{

    @Autowired
    private AuthUserService authUserService;

    /**
     * 创建租客用户
     *
     * @param phone    手机号码
     * @param realName 租客名称
     * @param user     操作者
     * @return 用户id
     */
    @Override
    public ResponseEntity<?> createTenantUser(String phone, String realName, JwtUser user) {
        try {
            String passwordMd5 = MD5.create().digestHex(phone.substring(phone.length() - 6));
            AddUserDTO dto = AddUserDTO.builder()
                    .username(phone)
                    .realName(realName)
                    .phone(phone)
                    .password(passwordMd5)
                    .role(Role.TENANT.roleName())
                    .build();
            authUserService.addUser(dto);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.error("添加租客用户异常", e);
            return ResponseEntity.fail(e.getMessage());
        }
    }
}
