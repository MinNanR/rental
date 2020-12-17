package site.minnan.rental.userinterface.fascade;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.UserService;
import site.minnan.rental.domain.vo.LoginVO;
import site.minnan.rental.userinterface.dto.PasswordLoginDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("rental/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @PostMapping("login/password")
    public ResponseEntity<LoginVO> loginPassword(@RequestBody @Valid PasswordLoginDTO dto) throws AuthenticationException {
        log.info("用户登录，登录信息：{}", new JSONObject(dto));
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new DisabledException("用户被禁用", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("用户名或密码错误", e);
        }
        LoginVO vo = userService.generateLoginVO(authentication);
        return ResponseEntity.success(vo);
    }

}
