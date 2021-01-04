package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 房客下拉框参数
 * @author Minnan on 2021/01/04
 */
@Data
public class GetTenantDropDownDTO {

    private String name;

    private Integer roomId;
}
