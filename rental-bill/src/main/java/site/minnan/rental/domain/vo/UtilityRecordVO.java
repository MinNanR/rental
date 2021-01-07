package site.minnan.rental.domain.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.Utility;

@Data
@Builder
public class UtilityRecordVO {

    private Integer id;

    private String roomNumber;

    public static UtilityRecordVO assemble(Utility utility){
        return UtilityRecordVO.builder()
                .id(utility.getId())
                .roomNumber(StrUtil.format("{}-{}", utility.getHouseName(), utility.getRoomNumber()))
                .build();
    }
}
