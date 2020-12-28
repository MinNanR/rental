package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.TenantInfoVO;
import site.minnan.rental.domain.vo.TenantVO;
import site.minnan.rental.userinterface.dto.AddTenantDTO;
import site.minnan.rental.userinterface.dto.DetailsQueryDTO;
import site.minnan.rental.userinterface.dto.GetTenantListDTO;

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
    void addTenant(AddTenantDTO dto);

    /**
     * 列表查询房客
     *
     * @param dto
     * @return
     */
    ListQueryVO<TenantVO> getTenantList(GetTenantListDTO dto);

    /**
     * 查询房客详情
     *
     * @param dto
     * @return
     */
    TenantInfoVO getTenantInfo(DetailsQueryDTO dto);
}
