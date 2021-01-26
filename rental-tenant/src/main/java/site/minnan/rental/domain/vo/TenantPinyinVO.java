package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 按拼音分类
 *
 * @author Minnan on 2021/1/26
 */
@Data
@AllArgsConstructor
public class TenantPinyinVO {

    private Character key;

    List<TenantAppVO> tenantList;
}
