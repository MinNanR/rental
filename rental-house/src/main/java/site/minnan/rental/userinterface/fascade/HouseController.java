package site.minnan.rental.userinterface.fascade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.userinterface.dto.GetHouseListDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("rental/house")
@Slf4j
public class HouseController {


    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getHouseList")
    public ResponseEntity<?> getHouseList(@RequestBody @Valid GetHouseListDTO dto){
        log.info("页码:" + dto.getPageIndex());
        return ResponseEntity.success();
    }
}
