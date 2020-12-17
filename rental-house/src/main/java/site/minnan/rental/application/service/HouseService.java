package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.HouseVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddHouseDTO;
import site.minnan.rental.userinterface.dto.GetHouseListDTO;

/**
 * 房屋service
 *
 * @author Minnan on 2020/12/17
 */
public interface HouseService {

    /**
     * 获取房屋列表
     *
     * @param dto 查询参数
     * @return
     */
    ListQueryVO<HouseVO> getHouseList(GetHouseListDTO dto);

    /**
     * 添加房屋
     *
     * @param dto 添加房屋参数
     */
    void addHouse(AddHouseDTO dto);
}
