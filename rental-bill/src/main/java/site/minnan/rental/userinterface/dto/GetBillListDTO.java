package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 获取账单列表参数
 * @author Minnan on 2021/1/12
 */
@Data
public class GetBillListDTO extends ListQueryDTO{

    private Integer houseId;

    private String roomNumber;

    private Integer year;

    private Integer month;

    private String status;
}
