package site.minnan.rental.application.service;

import site.minnan.rental.domain.vo.AuthUserVO;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.userinterface.dto.AddUserDTO;
import site.minnan.rental.userinterface.dto.GetUserListDTO;

/**
 * 用户管理service
 * create by Minnan on 2020/12/16
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
     * @param dto 添加用户参数
     */
    void addUser(AddUserDTO dto);

}
