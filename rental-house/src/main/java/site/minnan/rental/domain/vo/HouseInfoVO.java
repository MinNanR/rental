package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggretes.House;

/**
 * 房屋详情
 * @author Minnan on 2020/12/21
 */
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HouseInfoVO {

    private Integer id;

    private String address;

    private String directorName;

    private String directorPhone;

    private String longitude;

    private String latitude;

    private String updateUserName;

    private String updateTime;

    public static HouseInfoVO assemble(House house){
        return HouseInfoVO.builder()
                .id(house.getId())
                .address(house.getAddress())
                .directorName(house.getDirectorName())
                .directorPhone(house.getDirectorPhone())
                .longitude(house.getLongitude())
                .latitude(house.getLatitude())
                .updateUserName(house.getUpdateUserName())
                .updateTime(DateUtil.format(house.getUpdateTime(), "yyyy-MM-dd HH:mm"))
                .build();
    }
}