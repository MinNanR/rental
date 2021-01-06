package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 水电单状态下拉框
 * @author Minnan on 2021/01/06
 */
@Data
@AllArgsConstructor
public class UtilityStatusDropDown {

    private String value;

    private String status;
}
