package site.minnan.rental.userinterface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 结算账单时水电查询参数
 *
 * @author Minnan on 2021/1/22
 */
@Data
@AllArgsConstructor
public class SettleQueryDTO {

    private Integer roomId;

    private Integer startUtilityId;
}
