package site.minnan.rental.application.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.service.UserService;
import site.minnan.rental.domain.aggretes.AuthUser;
import site.minnan.rental.domain.enitty.JwtUser;
import site.minnan.rental.domain.mapper.UserMapper;
import site.minnan.rental.domain.vo.LoginVO;
import site.minnan.rental.infrastructure.utils.JwtUtil;
import site.minnan.rental.infrastructure.utils.RedisUtil;

import java.util.*;

/**
 * 用户权限service
 *
 * @author Minnan on 2020/12/16
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> userOptional = getAuthUser(username);
        AuthUser authUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
        String roleName = authUser.getRoleName();
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(roleName));
        return JwtUser.builder()
                .id(authUser.getId())
                .username(username)
                .password(authUser.getPassword())
                .authorities(grantedAuthorities)
                .enabled(authUser.getEnabled().equals(1))
                .realName(authUser.getRealName())
                .build();
    }

    /**
     * 生成登录token
     *
     * @param authentication
     * @return
     */
    @Override
    public LoginVO generateLoginVO(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(jwtUser);
        return new LoginVO(token);
    }

    private Optional<AuthUser> getAuthUser(String username) {
        if (StrUtil.isBlank(username)) return Optional.empty();
        AuthUser userInRedis = (AuthUser) redisUtil.getValue("authUser::" + username);
        if (userInRedis != null) {
            log.info("从redis中取出用户信息: {}", new JSONObject(userInRedis));
            return Optional.of(userInRedis);
        }
        QueryWrapper<AuthUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        AuthUser userInDB = userMapper.selectOne(wrapper);
        Optional<AuthUser> userOptional = Optional.ofNullable(userInDB);
        userOptional.ifPresent(user -> redisUtil.valueSet("authUser::" + username, user));
        return userOptional;
    }
}
