package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityRecordVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.userinterface.dto.*;

import java.util.List;

public interface UtilityService {

    /**
     * 登记水电（批量）
     *
     * @param dtoList
     */
    void addUtilityBatch(List<AddUtilityDTO> dtoList);

    /**
     * 登记水电（单个房间）
     * @param dto
     */
    void addUtility(AddUtilityDTO dto);

    /**
     * 修改水电记录
     *
     * @param dto
     */
    void updateUtility(UpdateUtilityDTO dto);

    /**
     * 获取水电记录
     *
     * @param dto
     * @return
     */
    ListQueryVO<UtilityVO> getUtilityList(GetUtilityDTO dto);

    /**
     * 查询登记水电的记录
     *
     * @param dto
     * @return
     */
    ListQueryVO<UtilityRecordVO> getRecordList(GetRecordListDTO dto);
}
