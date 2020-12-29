package site.minnan.rental.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.minnan.rental.application.ConfigService;
import site.minnan.rental.domain.aggregate.Menu;
import site.minnan.rental.domain.mapper.MenuMapper;
import site.minnan.rental.userinterface.dto.AddMenuDTO;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 添加菜单（仅测试用）
     *
     * @param dto
     */
    @Override
    public void addMenu(AddMenuDTO dto) {
        Menu menu = Menu.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .icon(dto.getIcon())
                .role("ADMIN")
                .build();
        menuMapper.insert(menu);
    }

    /**
     * 获取菜单
     *
     * @return
     */
    @Override
    public List<Menu> getMenu() {
        return menuMapper.selectList(null);
    }
}
