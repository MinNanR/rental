package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 账单状态下拉框
 * @author Minnan on 2021/1/11
 */
@Data
@AllArgsConstructor
public class BillStatusDropDown {

    private String status;

    private String value;
}
