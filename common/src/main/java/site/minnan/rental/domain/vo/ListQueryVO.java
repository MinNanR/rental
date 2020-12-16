package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 列表查询返回值基类
 * @param <T> 列表内容
 * created by Minnan on 2020/12/16
 */
@Getter
@AllArgsConstructor
public class ListQueryVO<T>{

    /**
     * 列表
     */
    List<T> list;

    /**
     * 总数量
     */
    Long totalCount;
}
