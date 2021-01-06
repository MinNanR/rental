package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 查询水电单参数
 */
@Data
public class GetUtilityListDTO extends ListQueryDTO{

    private Integer year;

    private Integer month;

    private Integer houseId;

    private String roomNumber;

    private String status;
}
