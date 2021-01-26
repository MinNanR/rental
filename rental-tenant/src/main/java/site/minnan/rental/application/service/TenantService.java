package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.*;
import site.minnan.rental.userinterface.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 房客相关服务
 *
 * @author Minnan on 2020/12/28
 */
public interface TenantService {

    /**
     * 添加房客
     *
     * @param dto
     */
    void addTenant(RegisterAddTenantDTO dto);

    /**
     * 列表查询房客
     *
     * @param dto
     * @return
     */
    ListQueryVO<TenantVO> getTenantList(GetTenantListDTO dto);

    /**
     * 查询该房间所住的房客
     *
     * @param dto
     * @return
     */
    List<TenantVO> getTenantByRoom(GetTenantByRoomDTO dto);

    /**
     * 查询房客详情
     *
     * @param dto
     * @return
     */
    TenantInfoVO getTenantInfo(DetailsQueryDTO dto);

    /**
     * 获取房客下拉框
     *
     * @param dto
     * @return
     */
    List<TenantDropDownVO> getTenantDropDown(GetTenantDropDownDTO dto);

    /**
     * 房客迁移房间
     *
     * @param dto
     */
    void tenantMove(TenantMoveDTO dto);

    /**
     * 修改房客信息
     *
     * @param dto
     */
    void updateTenant(UpdateTenantDTO dto);

    /**
     * 房客退租
     *
     * @param dto
     */
    void surrender(SurrenderDTO dto);

    /**
     * 检查身份证号码是否存在
     *
     * @param dto
     * @return
     */
    Boolean checkIdentificationNumberExist(CheckIdentificationNumberDTO dto);

    /**
     * 获取房客列表
     *
     * @return
     */
    List<TenantPinyinVO> getTenantList();
}
