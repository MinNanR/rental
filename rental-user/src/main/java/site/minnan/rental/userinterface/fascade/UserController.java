package site.minnan.rental.userinterface.fascade;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.AuthUserService;
import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/rental/user")
@Slf4j
public class UserController {

    @Autowired
    private AuthUserService authUserService;

    @RequestMapping("getUserList")
    public ResponseEntity<ListQueryVO<AuthUserVO>> getUserList(@RequestBody @Valid GetUserListDTO dto){
        ListQueryVO<AuthUserVO> vo = authUserService.getUserList(dto);
        return ResponseEntity.success(vo);
    }

    @RequestMapping("addUser/{type}")
    public ResponseEntity<?> addUser(@RequestBody @Valid AddUserDTO dto, @PathVariable("type") String type){
        dto.setRole(type);
        authUserService.addUser(dto);
        return ResponseEntity.success();
    }
}
