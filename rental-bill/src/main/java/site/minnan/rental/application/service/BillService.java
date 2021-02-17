package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.BillInfoVO;
import site.minnan.rental.domain.vo.BillVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityPrice;
import site.minnan.rental.userinterface.dto.*;

import java.io.IOException;
import java.math.BigDecimal;

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
     * 获取房间账单列表
     */
    ListQueryVO<BillVO> getRoomBillList(GetBillListDTO dto);


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
     * 生成收据
     *
     * @param dto
     */
    String createReceipt(DetailsQueryDTO dto) throws IOException;

    /**
     * 矫正水电读数
     *
     * @param dto
     */
    void correctUtility(DetailsQueryDTO dto);

    /**
     * 确认账单无误
     *
     * @param dto
     */
    void confirmBill(DetailsQueryDTO dto);

    /**
     * 房客已支付
     * @param dto
     */
    void billPaid(BillPaidDTO dto);

    /**
     * 到期账单结算
     */
    void setBillUnpaid();
}
