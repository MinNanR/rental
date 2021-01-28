package site.minnan.rental.application.provider;

import site.minnan.rental.userinterface.dto.AddTenantUserDTO;
import site.minnan.rental.userinterface.dto.BatchDisableUserDTO;
import site.minnan.rental.userinterface.dto.DisableTenantUserDTO;
import site.minnan.rental.userinterface.dto.EnableTenantUserBatchDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.util.List;

/**
 * 用户服务
 *
 * @author Minnan on 2020/12/28
 */
public interface UserProviderService {

    /**
     * 创建租客用户
     *
     * @param dto 参数
     * @return 新用户id
     */
    ResponseEntity<Integer> createTenantUser(AddTenantUserDTO dto);

    /**
     * 批量创建租客用户
     *
     * @param userList
     * @return
     */
    List<Integer> createTenantUserBatch(List<AddTenantUserDTO> userList);

    /**
     * 房客退租后禁用用户
     *
     * @param dto
     */
    void disableTenantUser(DisableTenantUserDTO dto);

    /**
     * 房客退租后批量禁用用户
     *
     * @param dto
     */
    void disableTenantUserBatch(BatchDisableUserDTO dto);

    /**
     * 启用用户（租客退租后重新入住）
     *
     * @param dto
     */
    void enableTenantUserBatch(EnableTenantUserBatchDTO dto);
}
