package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.*;
import site.minnan.rental.userinterface.dto.*;

import java.util.Collection;
import java.util.List;

/**
 * 账单服务
 *
 * @author Minnan on 2021/01/08
 */
public interface BillService {

    /**
     * 登记水电用量
     *
     * @param recordList
     */
    void recordUtility(List<RecordUtilityDTO> recordList);

    /**
     * 获取未登记水电的房间
     *
     * @param dto
     * @return
     */
    List<UnrecordedBillVO> getUnrecordedBill(GetUnrecordedBillDTO dto);

    /**
     * 修改水电量
     *
     * @param dto
     */
    void updateUtility(UpdateUtilityDTO dto);

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
     * 获取水电记录列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<UtilityVO> getUtilityList(GetUtilityListDTO dto);

    /**
     * 获取楼层下拉框
     *
     * @param dto
     * @return
     */
    Collection<Integer> getFloorDropDown(GetFloorDropDownDTO dto);

    /**
     * 获取账单列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<BillVO> getBillList(GetBillListDTO dto);

    /**
     * 获取未结算的账单
     *
     * @param dto
     * @return
     */
    List<UtilityVO> getUnsettledBill(GetUnsettledBillDTO dto);
}
