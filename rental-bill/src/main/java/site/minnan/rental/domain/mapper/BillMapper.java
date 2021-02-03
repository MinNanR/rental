package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.BillDetails;
import site.minnan.rental.domain.entity.BillTenantEntity;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.BillType;

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
    void settleBatch(@Param("bills") Collection<? extends Bill> bills);

    /**
     * 批量插入
     *
     * @param bills
     */
    void insertBatch(@Param("bills") Collection<Bill> bills);

    /**
     * 查询账单
     *
     * @param status
     * @param start
     * @param pageSize
     * @return
     */
    List<BillTenantEntity> getBillList(@Param("status") BillStatus status, @Param("start") Integer start, @Param(
            "pageSize") Integer pageSize);

    /**
     * 计算数量
     *
     * @param status
     * @return
     */
    Long countBill(@Param("status") BillStatus status);

    /**
     * 获取账单所有信息
     *
     * @param id
     * @return
     */
    BillDetails getBillDetails(@Param("id") Integer id);

    /**
     * 获取处于初始化的账单
     *
     * @return
     */
    List<BillDetails> getInitBillList(@Param("status") BillStatus status, @Param("type") BillType type);

    /**
     * 根据房间id获取账单信息
     *
     * @param roomId
     * @return
     */
    BillDetails getBillDetailsByRoomId(@Param("id") Integer roomId);
}
