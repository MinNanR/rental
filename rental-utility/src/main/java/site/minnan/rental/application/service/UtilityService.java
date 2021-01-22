package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.UtilityVO;
import site.minnan.rental.userinterface.dto.AddUtilityDTO;
import site.minnan.rental.userinterface.dto.GetUtilityDTO;
import site.minnan.rental.userinterface.dto.UpdateUtilityDTO;

import java.util.List;

public interface UtilityService {

    /**
     * 登记水电
     *
     * @param dtoList
     */
    void addUtilityBatch(List<AddUtilityDTO> dtoList);


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
}
