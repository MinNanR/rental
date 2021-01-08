package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 水电记录列表查询参数
 * @author Minnan on 2021/1/8
 */
@Data
public class GetUtilityListDTO extends ListQueryDTO{

    private Integer houseId;

    private String roomNumber;

    private Integer year;

    private Integer month;

    private String status;
}
