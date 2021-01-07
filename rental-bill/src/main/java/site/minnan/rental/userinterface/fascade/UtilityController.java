package site.minnan.rental.userinterface.fascade;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.UtilityService;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityRecordVO;
import site.minnan.rental.domain.vo.UtilityStatusDropDown;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.AddUtilityBatchDTO;
import site.minnan.rental.userinterface.dto.GetUtilityListDTO;
import site.minnan.rental.userinterface.dto.GetUtilityToBeRecordedDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 水电登记相关操作
 */
@RestController
@RequestMapping("rental/utility")
public class UtilityController {

    @Autowired
    private UtilityService utilityService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("addUtilityBatch")
    public ResponseEntity<?> addUtilityBatch(@RequestBody @Valid AddUtilityBatchDTO dto) {
        utilityService.addUtilityBatch(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityList")
    public ResponseEntity<ListQueryVO<UtilityVO>> getUtilityList(@RequestBody @Valid GetUtilityListDTO dto){
        ListQueryVO<UtilityVO> vo = utilityService.getUtilityList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateUtility")
    public ResponseEntity<?> updateUtility(@RequestBody @Valid UpdateUtilityDTO dto){
        utilityService.updateUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityStatusDropDown")
    public ResponseEntity<List<UtilityStatusDropDown>> getUtilityStatusDropDown(){
        List<UtilityStatusDropDown> dropDown = Arrays.stream(ArrayUtil.sub(UtilityStatus.values(), 2, 3))
                .map(e -> new UtilityStatusDropDown(e.getValue(), e.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.success(dropDown);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityToBeRecorded")
    public ResponseEntity<List<UtilityRecordVO>> getUtilityToBeRecorded(@RequestBody @Valid GetUtilityToBeRecordedDTO dto){
        List<UtilityRecordVO> list = utilityService.getUtilityRecord(dto);
        return ResponseEntity.success(list);
    }
}
