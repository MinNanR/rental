package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 房客已支付更新参数
 *
 * @author Minnan on 2021/2/4
 */
@Data
public class BillPaidDTO {

    private Integer id;

    private String paymentMethod;

    private String payTime;
}
