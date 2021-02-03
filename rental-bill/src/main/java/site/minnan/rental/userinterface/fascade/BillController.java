package site.minnan.rental.userinterface.fascade;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.userinterface.dto.*;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 水电登记相关操作
 */
@RestController
@RequestMapping("rental/bill")
@Slf4j
public class BillController {

    @Autowired
    private BillService billService;


    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("settleBill")
    public ResponseEntity<?> settleBill(@RequestBody @Valid SettleBillDTO dto) {
        billService.settleBill(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','LANDLORD')")
    @PostMapping("setUtilityPrice")
    public ResponseEntity<?> setUtilityPrice(@RequestBody SetUtilityPriceDTO dto) {
        billService.setUtilityPrice(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityPrice")
    public ResponseEntity<UtilityPrice> getUtilityPrice() {
        UtilityPrice utilityPrice = billService.getUtilityPrice();
        return ResponseEntity.success(utilityPrice);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getUtilityStatusDropDown")
    public ResponseEntity<List<BillStatusDropDown>> getUtilityStatusDropDown() {
        List<BillStatusDropDown> dropDownList = Arrays.stream(ArrayUtil.sub(BillStatus.values(), 2,
                3))
                .map(e -> new BillStatusDropDown(e.getStatus(), e.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.success(dropDownList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getBillStatusDropDown")
    public ResponseEntity<List<BillStatusDropDown>> getBillStatusDropDown() {
        List<BillStatusDropDown> dropDownList = Arrays.stream(ArrayUtil.sub(BillStatus.values(), 3,
                BillStatus.values().length))
                .map(e -> new BillStatusDropDown(e.getStatus(), e.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.success(dropDownList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getBillList/{status}")
    public ResponseEntity<ListQueryVO<BillVO>> getBillList(@RequestBody @Valid GetBillListDTO dto,
                                                           @PathVariable("status") String status) {
        dto.setStatus(BillStatus.valueOf(status.toUpperCase()));
        ListQueryVO<BillVO> vo = billService.getBillList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getBillList")
    public ResponseEntity<ListQueryVO<BillVO>> getBillList(@RequestBody @Valid ListQueryDTO dto) {
        ListQueryVO<BillVO> vo = billService.getBillList(dto);
        return ResponseEntity.success(vo);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getMonthTotal")
    public ResponseEntity<String> getMonthTotal() {
        BigDecimal total = billService.getMonthTotal();
        String totalStr = NumberUtil.decimalFormat(",###", total);
        return ResponseEntity.success(totalStr);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getBillInfo")
    public ResponseEntity<BillInfoVO> getBillInfo(@RequestBody @Valid DetailsQueryDTO dto) {
        BillInfoVO vo = billService.getBillInfo(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("createReceipt")
    public ResponseEntity<String> createReceipt(@RequestBody @Valid DetailsQueryDTO dto) {
        try {
            String url = billService.createReceipt(dto);
            return ResponseEntity.success(url);
        } catch (IOException e) {
            log.error("生成收据失败：id={}", dto.getId());
            return ResponseEntity.fail("生成收据失败");
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("correctBill")
    public ResponseEntity<?> correctUtility(@RequestBody @Valid DetailsQueryDTO dto) {
        billService.correctUtility(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("confirmBill")
    public ResponseEntity<?> confirm(@RequestBody @Valid DetailsQueryDTO dto){
        billService.confirmBill(dto);
        return ResponseEntity.success();
    }
}
