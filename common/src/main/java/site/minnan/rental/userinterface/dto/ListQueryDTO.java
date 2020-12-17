package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 列表查询参数基类
 * @author Minnan on 2020/12/16
 */
@Data
public class ListQueryDTO {

    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    @NotNull(message = "显示数量不能为空")
    private Integer pageSize;
}
