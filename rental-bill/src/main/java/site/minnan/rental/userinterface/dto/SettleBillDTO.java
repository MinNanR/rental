package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 结算账单参数
 *
 * @author Minnan on 2021/1/8
 */
@Data
public class SettleBillDTO {

    @NotEmpty(message = "请指定要结算的账单")
    private List<Integer> billIdList;
}
