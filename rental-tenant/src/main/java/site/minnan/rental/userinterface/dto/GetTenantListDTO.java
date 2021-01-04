package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 房客列表查询参数
 * @author Minnan on 2020/12/28
 */
@Data
public class GetTenantListDTO extends ListQueryDTO{

    private String name;

    private String hometownProvince;

    private String hometownCity;

    private Integer houseId;

    private String roomNumber;

    private Integer roomId;

}
