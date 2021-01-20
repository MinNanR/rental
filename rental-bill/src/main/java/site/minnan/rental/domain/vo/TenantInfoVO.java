package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 房客信息
 * @author Minnan on 2021/1/20
 */
@Data
@AllArgsConstructor
public class TenantInfoVO {

    private String name;

    private String phone;
}
