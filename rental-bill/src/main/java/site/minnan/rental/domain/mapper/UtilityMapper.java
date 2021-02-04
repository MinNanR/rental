package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Utility;

import java.util.List;

@Mapper
@Repository
public interface UtilityMapper extends BaseMapper<Utility> {

    /**
     * 批量登记水电
     *
     * @param utilityList
     */
    void addUtilityBatch(@Param("list") List<Utility> utilityList);
}
