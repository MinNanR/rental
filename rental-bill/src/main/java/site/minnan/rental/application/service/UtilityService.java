package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.userinterface.dto.AddUtilityBatchDTO;
import site.minnan.rental.userinterface.dto.GetUtilityListDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;

/**
 * @author Minnan on 2020/01/06
 */
public interface UtilityService {

    /**
     * 批量登记水电（按楼层）
     *
     * @param dto
     */
    void addUtilityBatch(AddUtilityBatchDTO dto);

    /**
     * 查询水电记录
     *
     * @param dto
     * @return
     */
    ListQueryVO<UtilityVO> getUtilityList(GetUtilityListDTO dto);

    /**
     * 更新水电记录
     *
     * @param dto
     */
    void updateUtility(UpdateUtilityDTO dto);
}
