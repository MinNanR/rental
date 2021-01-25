package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.entity.BillTenantRelevance;

import java.util.List;

/**
 *
 * @author Minnan on 2021/1/25
 */
@Repository
@Mapper
public interface BillTenantRelevanceMapper extends BaseMapper<BillTenantRelevance> {

    /**
     * 批量插入关联关系
     * @param relevanceList
     */
    void insertBatch(@Param("list") List<BillTenantRelevance> relevanceList);
}
