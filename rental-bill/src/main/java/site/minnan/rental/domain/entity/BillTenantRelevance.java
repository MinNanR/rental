package site.minnan.rental.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@NoArgsConstructor
@TableName("rental_bill_tenant_relevance")
public class BillTenantRelevance {

    @TableId(type = IdType.AUTO)
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
