package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Utility;

import java.util.Collection;

/**
 * @author Minna on 2021/01/06
 */
@Mapper
@Repository
public interface UtilityMapper extends BaseMapper<Utility> {

    /**
     * 批量添加
     *
     * @param utilities
     * @return
     */
    int insertBatch(@Param("utilities")Collection<Utility> utilities);
}
