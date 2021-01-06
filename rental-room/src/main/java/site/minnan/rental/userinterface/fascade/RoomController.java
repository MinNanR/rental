package site.minnan.rental.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.rental.application.service.RoomService;
import site.minnan.rental.domain.vo.*;
import site.minnan.rental.infrastructure.enumerate.RoomStatus;
import site.minnan.rental.userinterface.dto.*;
import site.minnan.rental.userinterface.response.ResponseEntity;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Minnan on 2020/12/29
 */
@RestController
@RequestMapping("rental/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("addRoom")
    public ResponseEntity<?> addRoom(@RequestBody @Valid AddRoomDTO dto) {
        roomService.addRoom(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRoomList")
    public ResponseEntity<ListQueryVO<RoomVO>> getRoomList(@RequestBody @Valid GetRoomListDTO dto) {
        ListQueryVO<RoomVO> vo = roomService.getRoomList(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRoomInfo")
    public ResponseEntity<RoomInfoVO> getRoomInfo(@RequestBody @Valid DetailsQueryDTO dto) {
        RoomInfoVO vo = roomService.getRoomInfo(dto);
        return ResponseEntity.success(vo);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("updateRoom")
    public ResponseEntity<?> updateRoom(@RequestBody @Valid UpdateRoomDTO dto) {
        roomService.updateRoom(dto);
        return ResponseEntity.success();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("checkRoomNumberUsed")
    public ResponseEntity<Boolean> checkRoomNumberUsed(@RequestBody @Valid CheckRoomNumberDTO dto) {
        Boolean check = roomService.checkRoomNumberUsed(dto);
        return ResponseEntity.success(check);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRoomStatusDropDown")
    public ResponseEntity<List<RoomStatusDropDown>> getRoomStatus() {
        List<RoomStatusDropDown> dropDownList = Arrays.stream(RoomStatus.values())
                .filter(e -> !RoomStatus.DELETED.equals(e))
                .map(e -> new RoomStatusDropDown(e.getStatus(), e.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.success(dropDownList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRoomDropDown")
    public ResponseEntity<List<RoomDropDown>> getRoomDropDown(@RequestBody @Valid GetRoomDropDownDTO dto) {
        List<RoomDropDown> roomDropDown = roomService.getRoomDropDown(dto);
        return ResponseEntity.success(roomDropDown);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'LANDLORD')")
    @PostMapping("getRoomToRecordUtility")
    public ResponseEntity<List<RoomDropDown>> getRoomToRecordUtility(@RequestBody @Valid GetRoomToRecordUtilityDTO dto){
        List<RoomDropDown> roomList = roomService.getRoomToRecordUtility(dto);
        return ResponseEntity.success(roomList);
    }
}
