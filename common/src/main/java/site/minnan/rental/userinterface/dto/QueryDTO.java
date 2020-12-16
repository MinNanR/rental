package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryDTO {

    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    @NotNull(message = "显示数量不能为空")
    private Integer pageSize;
}
