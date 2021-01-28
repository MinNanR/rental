package site.minnan.rental.domain.entity;

import lombok.Data;
import site.minnan.rental.domain.aggregate.Bill;

import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * 账单房客连表查询
 * @author Minnan on 2021/01/28
 */
@Data
public class BillTenantEntity extends Bill {

    private String name;

    private String phone;
}
