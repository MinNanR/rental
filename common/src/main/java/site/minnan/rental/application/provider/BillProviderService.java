package site.minnan.rental.application.provider;

import site.minnan.rental.userinterface.dto.CreateBillDTO;

/**
 * 水电记录，账单服务
 */
public interface BillProviderService {

    /**
     * 房间由空闲转入在租状态时创建账单
     *
     * @param dto
     */
    void createBill(CreateBillDTO dto);

    /**
     * 房客退租时账单状态修改
     *
     * @param roomId
     */
    void completeBillWithSurrender(Integer roomId);
}
