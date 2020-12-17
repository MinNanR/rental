package site.minnan.rental.application.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import site.minnan.rental.domain.vo.LoginVO;

/**
 * 用户权限service
 * @author Minnan on 2020/12/16
 */
public interface UserService extends UserDetailsService {

    /**
     * 生成登录token
     * @param authentication
     * @return
     */
    LoginVO generateLoginVO(Authentication authentication);
}
