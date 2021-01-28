package site.minnan.rental.application.provider;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.domain.aggregate.AuthUser;
import site.minnan.rental.domain.mapper.UserMapper;
import site.minnan.rental.infrastructure.enumerate.Role;
import site.minnan.rental.userinterface.dto.AddTenantUserDTO;
import site.minnan.rental.userinterface.dto.BatchDisableUserDTO;
import site.minnan.rental.userinterface.dto.DisableTenantUserDTO;
import site.minnan.rental.userinterface.dto.EnableTenantUserBatchDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(timeout = 30000, interfaceClass = UserProviderService.class)
@Slf4j
public class UserProviderServiceImpl implements UserProviderService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 创建租客用户
     *
     * @param dto 参数
     * @return 新用户id
     */
    @Override
    @Transactional
    public ResponseEntity<Integer> createTenantUser(AddTenantUserDTO dto) {
        try {
            Integer check = userMapper.checkUsernameUsed(dto.getPhone());
            if (check != null) {
                return ResponseEntity.fail("手机号码已存在");
            }
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
            return ResponseEntity.success(authUser.getId());
        } catch (Exception e) {
            log.error("添加租客用户异常", e);
            return ResponseEntity.fail(e.getMessage());
        }
    }

    /**
     * 房客退租后禁用用户
     *
     * @param dto
     */
    @Override
    public void disableTenantUser(DisableTenantUserDTO dto) {
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.set("enabled", AuthUser.DISABLE)
                .set("update_user_id", dto.getUpdateUserId())
                .set("update_user_name", dto.getUpdateUserName())
                .set("update_time", new Timestamp(System.currentTimeMillis()))
                .eq("id", dto.getUserId());
        userMapper.update(null, wrapper);
    }

    /**
     * 房客退租后批量禁用用户
     *
     * @param dto
     */
    @Override
    public void disableTenantUserBatch(BatchDisableUserDTO dto) {
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.set("enabled", AuthUser.DISABLE)
                .set("update_user_id", dto.getUserId())
                .set("update_user_name", dto.getUserName())
                .set("update_time", new Timestamp(System.currentTimeMillis()))
                .in("id", dto.getIdList());
        userMapper.update(null, wrapper);
    }

    /**
     * 启用用户（租客退租后重新入住）
     *
     * @param dto
     */
    @Override
    public void enableTenantUserBatch(EnableTenantUserBatchDTO dto) {
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.set("enabled", 1)
                .set("update_user_id", dto.getUserId())
                .set("update_user_name", dto.getUserName())
                .set("update_time", new Timestamp(System.currentTimeMillis()))
                .in("id", dto.getUserIdList());
        userMapper.update(null, wrapper);
    }

    /**
     * 批量创建租客用户
     *
     * @param userList
     * @return
     */
    @Override
    public List<Integer> createTenantUserBatch(List<AddTenantUserDTO> userList) {
        List<AuthUser> newUserList = new ArrayList<>();
        for (AddTenantUserDTO dto : userList) {
            String passwordMd5 = MD5.create().digestHex(dto.getPhone().substring(dto.getPhone().length() - 6));
            String encodedPassword = passwordEncoder.encode(passwordMd5);
            AuthUser authUser = AuthUser.builder()
                    .username(dto.getPhone())
                    .password(encodedPassword)
                    .phone(dto.getPhone())
                    .realName(dto.getRealName())
                    .role(Role.TENANT)
                    .enabled(AuthUser.ENABLE)
                    .build();
            authUser.setCreateUser(dto.getUserId(), dto.getUserName());
            newUserList.add(authUser);
        }
        userMapper.addUserBatch(newUserList);
        return newUserList.stream().map(AuthUser::getId).collect(Collectors.toList());
    }
}
