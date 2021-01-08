package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.userinterface.dto.*;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 水电登记相关操作
 */
@RestController
@RequestMapping("rental/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("recordUtility")
    public ResponseEntity<?> recordUtility(@RequestBody @Valid List<RecordUtilityDTO> dtoList) {
        billService.recordUtility(dtoList);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUnrecordedBill")
    public ResponseEntity<List<UnrecordedBillVO>> getUnrecordedBill(@RequestBody @Valid GetUnrecordedBillDTO dto) {
        List<UnrecordedBillVO> billList = billService.getUnrecordedBill(dto);
        return ResponseEntity.success(billList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateUtility")
    public ResponseEntity<?> updateUtility(@RequestBody @Valid UpdateUtilityDTO dto){
        billService.updateUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("settleBill")
    public ResponseEntity<?> settleBill(@RequestBody @Valid SettleBillDTO dto){
        billService.settleBill(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("setUtilityPrice")
    public ResponseEntity<?> setUtilityPrice(@RequestBody SetUtilityPriceDTO dto){
        billService.setUtilityPrice(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityPrice")
    public ResponseEntity<UtilityPrice> getUtilityPrice(){
        UtilityPrice utilityPrice = billService.getUtilityPrice();
        return ResponseEntity.success(utilityPrice);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("getUtilityList")
    public ResponseEntity<ListQueryVO<UtilityVO>> getUtilityList(@RequestBody @Valid GetUtilityListDTO dto){
        ListQueryVO<UtilityVO> vo = billService.getUtilityList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getFloorDropDown")
    public ResponseEntity<Collection<Integer>> getFloorDropDown(@RequestBody @Valid GetFloorDropDownDTO dto){
        Collection<Integer> floorDropDown = billService.getFloorDropDown(dto);
        return ResponseEntity.success(floorDropDown);
    }
}
