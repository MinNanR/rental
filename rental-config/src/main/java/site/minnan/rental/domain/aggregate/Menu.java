package site.minnan.rental.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 菜单实体类
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@TableName("auth_menu")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 路由地址
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单名称
     */
    private String name;


    /**
     * 所属角色
     */
    private String role;
}
