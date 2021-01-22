package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Bill;

import java.util.Collection;
import java.util.List;

/**
 * @author Minnan on 2021/01/06
 */
@Mapper
@Repository
public interface BillMapper extends BaseMapper<Bill> {

    /**
     * 批量结算
     *
     * @param bills
     */
    void settleBatch(@Param("bills") Collection<Bill> bills);

    /**
     * 批量插入
     *
     * @param bills
     */
    void insertBatch(@Param("bills") Collection<Bill> bills);
}
