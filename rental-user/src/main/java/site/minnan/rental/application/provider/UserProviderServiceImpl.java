package site.minnan.rental.application.provider;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.domain.aggregate.AuthUser;
import site.minnan.rental.domain.mapper.UserMapper;
import site.minnan.rental.infrastructure.enumerate.Role;
import site.minnan.rental.userinterface.dto.AddTenantUserDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

@Service(timeout = 5000, interfaceClass = UserProviderService.class)
@Slf4j
public class UserProviderServiceImpl implements UserProviderService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 创建租客用户
     *
     * @param dto 参数
     */
    @Override
    @Transactional
    public ResponseEntity<?> createTenantUser(AddTenantUserDTO dto) {
        try {
            String passwordMd5 = MD5.create().digestHex(dto.getPhone().substring(dto.getPhone().length() - 6));
            String encodedPassword = passwordEncoder.encode(passwordMd5);
            AuthUser authUser = AuthUser.builder()
                    .username(dto.getPhone())
                    .password(encodedPassword)
                    .phone(dto.getPhone())
                    .realName(dto.getRealName())
                    .role(Role.TENANT)
                    .enabled(1)
                    .build();
            authUser.setCreateUser(dto.getUserId(), dto.getUserName());
            userMapper.insert(authUser);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.error("添加租客用户异常", e);
            return ResponseEntity.fail(e.getMessage());
        }
    }
}
