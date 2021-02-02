package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.*;
import site.minnan.rental.userinterface.dto.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 账单服务
 *
 * @author Minnan on 2021/01/08
 */
public interface BillService {

    /**
     * 结算账单
     *
     * @param dto
     */
    void settleBill(SettleBillDTO dto);

    /**
     * 设置水电单价
     *
     * @param dto
     */
    void setUtilityPrice(SetUtilityPriceDTO dto);

    /**
     * 获取水电单价
     *
     * @return
     */
    UtilityPrice getUtilityPrice();

    /**
     * 获取账单列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<BillVO> getBillList(GetBillListDTO dto);

    /**
     * 获取账单列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<BillVO> getBillList(ListQueryDTO dto);

    /**
     * 获取本月总额
     *
     * @return
     */
    BigDecimal getMonthTotal();

    /**
     * 获取账单详情
     *
     * @param dto
     * @return
     */
    BillInfoVO getBillInfo(DetailsQueryDTO dto);

    /**
     * 到期账单结算
     */
    void setBillUnpaid();
}
