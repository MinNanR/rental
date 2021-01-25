package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.minnan.rental.application.service.TenantService;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.TenantDropDownVO;
import site.minnan.rental.domain.vo.TenantInfoVO;
import site.minnan.rental.domain.vo.TenantVO;
import site.minnan.rental.userinterface.dto.*;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

/**
 * 房客信息控制器
 *
 * @author Minnan on 2020/12/21
 */
@RestController
@RequestMapping("rental/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("addTenant")
    public ResponseEntity<?> addTenant(@RequestBody @Valid RegisterAddTenantDTO dto) {
        tenantService.addTenant(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getTenantList")
    public ResponseEntity<ListQueryVO<TenantVO>> getTenantList(@RequestBody @Valid GetTenantListDTO dto) {
        ListQueryVO<TenantVO> vo = tenantService.getTenantList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getTenantByRoom")
    public ResponseEntity<List<TenantVO>> getTenantByRoom(@RequestBody @Valid GetTenantByRoomDTO dto) {
        List<TenantVO> tenantList = tenantService.getTenantByRoom(dto);
        return ResponseEntity.success(tenantList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getTenantInfo")
    public ResponseEntity<?> getTenantInfo(@RequestBody @Valid DetailsQueryDTO dto) {
        TenantInfoVO vo = tenantService.getTenantInfo(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getTenantDropDown")
    public ResponseEntity<List<TenantDropDownVO>> getTenantDropDown(@RequestBody @Valid GetTenantDropDownDTO dto) {
        List<TenantDropDownVO> dropDownList = tenantService.getTenantDropDown(dto);
        return ResponseEntity.success(dropDownList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("tenantMove")
    public ResponseEntity<?> tenantMove(@RequestBody @Valid TenantMoveDTO dto) {
        tenantService.tenantMove(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateTenant")
    public ResponseEntity<?> updateTenant(@RequestBody @Valid UpdateTenantDTO dto) {
        tenantService.updateTenant(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("surrender")
    public ResponseEntity<?> surrender(@RequestBody @Valid SurrenderDTO dto) {
        tenantService.surrender(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("checkIdNumber")
    public ResponseEntity<Boolean> checkIdentificationNumberExist(CheckIdentificationNumberDTO dto) {
        Boolean check = tenantService.checkIdentificationNumberExist(dto);
        return ResponseEntity.success(check);
    }
}
