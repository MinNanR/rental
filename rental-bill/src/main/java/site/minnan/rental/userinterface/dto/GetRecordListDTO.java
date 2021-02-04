package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 水电记录查询参数
 *
 * @author Minnan on 2021/1/27
 */
@Data
public class GetRecordListDTO extends ListQueryDTO {

    private Integer houseId;
}
