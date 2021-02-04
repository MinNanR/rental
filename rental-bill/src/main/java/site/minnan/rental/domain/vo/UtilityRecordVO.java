package site.minnan.rental.domain.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.entity.UtilityRecord;

/**
 * 水电单记录
 *
 * @author Minnan on 2021/1/27
 */
@Data
@Builder
public class UtilityRecordVO {

    private Integer id;

    private String name;

    private Integer houseId;

    private String date;

    public static UtilityRecordVO assemble(UtilityRecord record) {
        return UtilityRecordVO.builder()
                .id(record.getId())
                .name(record.getName())
                .houseId(record.getHouseId())
                .date(DateUtil.format(record.getRecordDate(), "yyyy-MM-dd"))
                .build();
    }
}
