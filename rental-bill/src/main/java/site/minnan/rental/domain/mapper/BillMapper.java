package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Bill;

/**
 * @author Minnan on 2021/01/06
 */
@Mapper
@Repository
public interface BillMapper extends BaseMapper<Bill> {
}
