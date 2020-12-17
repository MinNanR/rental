package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.UserEnabledDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;
import site.minnan.rental.userinterface.dto.UpdateUserDTO;

/**
 * 用户管理service
 *
 * @author Minnan on 2020/12/16
 */
public interface AuthUserService {

    /**
     * 查询用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    ListQueryVO<AuthUserVO> getUserList(GetUserListDTO dto);



    /**
     * 添加用户
     *
     * @param dto 添加用户参数
     */
    void addUser(AddUserDTO dto);

    /**
     * 更新用户信息
     *
     * @param dto 更新用户信息参数
     */
    void updateUser(UpdateUserDTO dto);

    /**
     * 禁用用户
     *
     * @param dto 参数
     */
    void disableUser(UserEnabledDTO dto);

    /**
     * 启用用户
     *
     * @param dto 参数
     */
    void enableUser(UserEnabledDTO dto);
}
