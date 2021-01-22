package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 水电单查询参数
 *
 * @author Minnan on 2021/1/22
 */
@Data
public class GetUtilityDTO extends ListQueryDTO {

    private Integer houseId;

    private String roomNumber;

    private Integer year;

    private Integer month;
}
