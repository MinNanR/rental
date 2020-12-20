package site.minnan.rental.application;

import site.minnan.rental.domain.aggretes.Menu;
import site.minnan.rental.userinterface.dto.AddMenuDTO;

import java.util.List;

/**
 * 获取菜单，权限等配置信息
 *
 * @author Minnan created on 2020/12/20
 */
public interface ConfigService {

    /**
     * 添加菜单（仅测试用）
     *
     * @param dto
     */
    void addMenu(AddMenuDTO dto);

    /**
     * 获取菜单
     *
     * @return
     */
    List<Menu> getMenu();
}
