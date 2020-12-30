package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 房间状态下拉框
 * @author Minnan on 2020/12/30
 */
@Data
@AllArgsConstructor
public class RoomStatusDropDown {

    private String status;

    private String value;
}
