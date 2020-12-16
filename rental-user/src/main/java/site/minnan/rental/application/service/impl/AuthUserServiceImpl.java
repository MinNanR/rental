package site.minnan.rental.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户管理service
 */
@Service
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 查询用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    @Override
    public ListQueryVO<AuthUserVO> getUserList(GetUserListDTO dto) {
        QueryWrapper<AuthUser> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getRealName()).ifPresent(e -> wrapper.eq("realName" ,e));
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(e -> wrapper.eq("phone",e));
        Page<AuthUser> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<AuthUser> authUserPage = userMapper.selectPage(page, wrapper);
        List<AuthUserVO> voList = authUserPage.getRecords().stream().map(AuthUserVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(voList, authUserPage.getTotal());
    }

    /**
     * 添加用户
     *
     * @param dto 添加用户参数
     */
    @Override
    public void addUser(AddUserDTO dto) {
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
        newUser.createUser(user);
        newUser.role(Role.valueOf(dto.getRole().toUpperCase()));
        userMapper.insert(newUser);
    }
}
