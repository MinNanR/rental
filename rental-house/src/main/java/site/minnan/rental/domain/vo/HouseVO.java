package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.minnan.rental.domain.aggretes.House;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HouseVO implements Serializable {

    private Integer id;

    private String address;

    private String directorName;

    private String updateUserName;

    private String updateTime;

    public static HouseVO assemble(House house){
        return HouseVO.builder()
                .id(house.getId())
                .address(house.getAddress())
                .directorName(house.getDirectorName())
                .updateUserName(house.getUpdateUserName())
                .updateTime(DateUtil.format(house.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }
}