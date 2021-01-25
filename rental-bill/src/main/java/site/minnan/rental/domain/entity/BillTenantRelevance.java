package site.minnan.rental.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 账单与房客关联关系
 *
 * @author Minnan on 2021/1/25
 */
@Getter
@AllArgsConstructor
public class BillTenantRelevance {

    private Integer id;

    /**
     * 账单id
     */
    private Integer billId;

    /**
     * 房客id
     */
    private Integer tenantId;

    public static BillTenantRelevance of(Integer billId, Integer tenantId) {
        return new BillTenantRelevance(null, billId, tenantId);
    }
}
