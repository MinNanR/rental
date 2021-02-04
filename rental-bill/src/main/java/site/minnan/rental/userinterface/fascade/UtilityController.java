package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.UtilityService;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityRecordVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;
import site.minnan.rental.userinterface.dto.GetRecordListDTO;
import site.minnan.rental.userinterface.dto.GetUtilityDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

/**
 * 水电相关操作
 *
 * @author Minnan on 2021/1/22
 */
@RestController
@RequestMapping("rental/utility")
public class UtilityController {

    @Autowired
    private UtilityService utilityService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("recordUtility/batch")
    public ResponseEntity<?> recordUtility(@RequestBody @Valid List<AddUtilityDTO> dtoList) {
        utilityService.addUtilityBatch(dtoList);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("recordUtility/single")
    public ResponseEntity<?> recordUtility(@RequestBody @Valid AddUtilityDTO dto){
        utilityService.addUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateUtility")
    public ResponseEntity<?> updateUtility(@RequestBody @Valid UpdateUtilityDTO dto) {
        utilityService.updateUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityList")
    public ResponseEntity<ListQueryVO<UtilityVO>> getUtilityList(@RequestBody @Valid GetUtilityDTO dto) {
        ListQueryVO<UtilityVO> vo = utilityService.getUtilityList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRecordList")
    public ResponseEntity<ListQueryVO<UtilityRecordVO>> getRecordList(@RequestBody @Valid GetRecordListDTO dto) {
        ListQueryVO<UtilityRecordVO> vo = utilityService.getRecordList(dto);
        return ResponseEntity.success(vo);
    }
}
