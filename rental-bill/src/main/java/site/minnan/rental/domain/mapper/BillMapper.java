package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.userinterface.dto.GetFloorDropDownDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Minnan on 2021/01/06
 */
@Mapper
@Repository
public interface BillMapper extends BaseMapper<Bill> {

    void updateBatch(@Param("bills") Collection<Bill> bills);

    void settleBatch(@Param("bills") Collection<Bill> bills);
}
