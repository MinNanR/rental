package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.AuthUserService;
import site.minnan.rental.domain.aggretes.AuthUser;
import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.domain.mapper.UserMapper;
import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.infrastructure.enumerate.Role;
import site.minnan.rental.infrastructure.exception.EntityAlreadyExistException;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.infrastructure.utils.RedisUtil;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.UserEnabledDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;
import site.minnan.rental.userinterface.dto.UpdateUserDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户管理service
 * @author Minnan on 2020/12/16
 */
@Service
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 查询用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    @Override
    public ListQueryVO<AuthUserVO> getUserList(GetUserListDTO dto) {
        QueryWrapper<AuthUser> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getRealName()).ifPresent(e -> wrapper.eq("realName", e));
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(e -> wrapper.eq("phone", e));
        Page<AuthUser> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<AuthUser> authUserPage = userMapper.selectPage(page, wrapper);
        List<AuthUserVO> voList =
                authUserPage.getRecords().stream().map(AuthUserVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(voList, authUserPage.getTotal());
    }

    /**
     * 添加用户
     *
     * @param dto 添加用户参数
     */
    @Override
    public void addUser(AddUserDTO dto) throws EntityAlreadyExistException {
        QueryWrapper<AuthUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        AuthUser check = userMapper.selectOne(wrapper);
        if (check != null) {
            throw new EntityAlreadyExistException("用户名已被使用");
        }
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        String rawPassword = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        AuthUser newUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .realName(dto.getRealName())
                .phone(dto.getPhone())
                .createTime(current)
                .updateTime(current).build();
        newUser.setCreateUser(user);
        newUser.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        userMapper.insert(newUser);
    }

    /**
     * 更新用户信息
     *
     * @param dto 更新用户信息参数
     */
    @Override
    public void updateUser(UpdateUserDTO dto) {
        AuthUser authUser = userMapper.selectById(dto.getId());
        if (authUser == null) {
            throw new EntityNotExistException("用户不存在");
        }
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", dto.getId());
        Optional.ofNullable(dto.getPassword()).ifPresent(s -> wrapper.set("password", passwordEncoder.encode(s)));
        Optional.ofNullable(dto.getRealName()).ifPresent(s -> wrapper.set("real_name", s));
        Optional.ofNullable(dto.getPhone()).ifPresent(s -> wrapper.set("phone", s));
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wrapper.set("update_user_id", principal.getId())
                .set("update_user_name", principal.getRealName())
                .set("update_time", new Timestamp(System.currentTimeMillis()));
        redisUtil.delete("authUser::" + authUser.getUsername());
        userMapper.update(null, wrapper);
    }

    /**
     * 禁用用户
     *
     * @param dto 参数
     */
    @Override
    public void disableUser(UserEnabledDTO dto) {
        AuthUser authUser = userMapper.selectById(dto.getId());
        if(authUser == null){
            throw new EntityNotExistException("用户不存在");
        }
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", dto.getId());
        wrapper.set("enabled", AuthUser.DISABLE);
        redisUtil.delete("authUser::" + authUser.getUsername());
        userMapper.update(null, wrapper);
    }

    /**
     * 启用用户
     *
     * @param dto 参数
     */
    @Override
    public void enableUser(UserEnabledDTO dto) {
        AuthUser authUser = userMapper.selectById(dto.getId());
        if(authUser == null){
            throw new EntityNotExistException("用户不存在");
        }
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", dto.getId());
        wrapper.set("enabled", AuthUser.ENABLE);
        redisUtil.delete("authUser::" + authUser.getUsername());
        userMapper.update(null, wrapper);
    }
}
