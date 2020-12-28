package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.TenantService;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.TenantInfoVO;
import site.minnan.rental.domain.vo.TenantVO;
import site.minnan.rental.userinterface.dto.AddTenantDTO;
import site.minnan.rental.userinterface.dto.DetailsQueryDTO;
import site.minnan.rental.userinterface.dto.GetTenantListDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;

/**
 * 房客信息控制器
 * @author Minnan on 2020/12/21
 */
@RestController
@RequestMapping("rental/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @RequestMapping("addTenant")
    public ResponseEntity<?> addTenant(@RequestBody @Valid AddTenantDTO dto){
        tenantService.addTenant(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @RequestMapping("getTenantList")
    public ResponseEntity<?> getTenantList(@RequestBody @Valid GetTenantListDTO dto){
        ListQueryVO<TenantVO> vo = tenantService.getTenantList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @RequestMapping("getTenantInfo")
    public ResponseEntity<?> getTenantInfo(@RequestBody @Valid DetailsQueryDTO dto){
        TenantInfoVO vo = tenantService.getTenantInfo(dto);
        return ResponseEntity.success(vo);
    }
}
