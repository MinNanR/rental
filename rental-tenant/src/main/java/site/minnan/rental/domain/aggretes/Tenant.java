package site.minnan.rental.domain.aggretes;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 房客信息
 * @author Minnan on 2020/12/21
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("rental_tenant")
public class Tenant {

}
