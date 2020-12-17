package site.minnan.rental.userinterface.fascade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.minnan.rental.application.service.AuthUserService;
import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.UserEnabledDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;
import site.minnan.rental.userinterface.dto.UpdateUserDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/rental/user")
@Slf4j
public class UserController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("getUserList")
    public ResponseEntity<ListQueryVO<AuthUserVO>> getUserList(@RequestBody @Valid GetUserListDTO dto) {
        ListQueryVO<AuthUserVO> vo = authUserService.getUserList(dto);
        return ResponseEntity.success(vo);
    }

    @PostMapping("addUser/{type}")
    public ResponseEntity<?> addUser(@RequestBody @Valid AddUserDTO dto, @PathVariable("type") String type) {
        dto.setRole(type);
        authUserService.addUser(dto);
        return ResponseEntity.success();
    }

    @PostMapping("updateUser")
    public ResponseEntity<?> updateUser(@RequestBody @Validated UpdateUserDTO dto){
        authUserService.updateUser(dto);
        return ResponseEntity.success();
    }

    @PostMapping("disableUser")
    public ResponseEntity<?> disableUser(@RequestBody @Valid UserEnabledDTO dto){
        authUserService.disableUser(dto);
        return ResponseEntity.success();
    }

    @PostMapping("enableUser")
    public ResponseEntity<?> enableUser(@RequestBody @Valid UserEnabledDTO dto){
        authUserService.enableUser(dto);
        return ResponseEntity.success();
    }
}
