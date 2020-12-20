package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.ConfigService;
import site.minnan.rental.domain.aggretes.Menu;
import site.minnan.rental.userinterface.dto.AddMenuDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.util.List;

/**
 * 基础配置controller（下拉框，菜单等）
 * @author Minnan created by 2020/12/20
 */
@RestController
@RequestMapping("rental/common")
public class CommonController {

    @Autowired
    private ConfigService configService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("addMenu")
    public ResponseEntity<?> addMenu(@RequestBody AddMenuDTO dto){
        configService.addMenu(dto);
        return ResponseEntity.success();
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("getMenu")
    public ResponseEntity<List<Menu>> getMenu(){
        List<Menu> menuList = configService.getMenu();
        return ResponseEntity.success(menuList);
    }
}
