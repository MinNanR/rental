package site.minnan.rental.userinterface.fascade;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.pinyin.PinyinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.UtilityService;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;
import site.minnan.rental.userinterface.dto.GetUtilityDTO;
import site.minnan.rental.userinterface.dto.ListQueryDTO;
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
    @PostMapping("recordUtility")
    public ResponseEntity<?> recordUtility(@RequestBody @Valid List<AddUtilityDTO> dtoList){
        utilityService.addUtilityBatch(dtoList);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateUtility")
    public ResponseEntity<?> updateUtility(@RequestBody @Valid UpdateUtilityDTO dto){
        utilityService.updateUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityList")
    public ResponseEntity<ListQueryVO<UtilityVO>> getUtilityList(@RequestBody @Valid GetUtilityDTO dto){
        ListQueryVO<UtilityVO> vo = utilityService.getUtilityList(dto);
        return ResponseEntity.success(vo);
    }


}
