package site.minnan.rental.domain.vo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 楼层下拉框
 * @author Minnan on 2021/01/27
 */
@Data
public class FloorDropDown {

    private Integer houseId;

    private String houseName;

    private List<Integer> floorList;

    public FloorDropDown(Integer houseId, String houseName, List<Integer> floors){
        this.houseId = houseId;
        this.houseName = houseName;
        this.floorList = floors;
    }
}
