package site.minnan.rental.userinterface.fascade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.minnan.rental.application.service.AuthUserService;
import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UserInfoVO;
import site.minnan.rental.userinterface.dto.*;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/rental/user")
@Slf4j
public class UserController {

    @Autowired
    private AuthUserService authUserService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("getUserList")
    public ResponseEntity<ListQueryVO<AuthUserVO>> getUserList(@RequestBody @Valid GetUserListDTO dto) {
        ListQueryVO<AuthUserVO> vo = authUserService.getUserList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("addUser/{type}")
    public ResponseEntity<?> addUser(@RequestBody @Valid AddUserDTO dto, @PathVariable("type") String type) {
        dto.setRole(type);
        authUserService.addUser(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateUser")
    public ResponseEntity<?> updateUser(@RequestBody @Validated UpdateUserDTO dto) {
        authUserService.updateUser(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("disableUser")
    public ResponseEntity<?> disableUser(@RequestBody @Valid UserEnabledDTO dto) {
        authUserService.disableUser(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("enableUser")
    public ResponseEntity<?> enableUser(@RequestBody @Valid UserEnabledDTO dto) {
        authUserService.enableUser(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("getUserInfo")
    public ResponseEntity<UserInfoVO> getUserInfo(@RequestBody @Valid DetailsQueryDTO dto) {
        UserInfoVO userInfo = authUserService.getUserInfo(dto);
        return ResponseEntity.success(userInfo);
    }
}
