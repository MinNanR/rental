package site.minnan.rental.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * 水电记录
 *
 * @author Minnan on 2021/1/27
 */
@Builder
@Getter
@TableName("rental_utility_record")
@AllArgsConstructor
public class UtilityRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房屋id
     */
    private Integer houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 记录标题
     */
    private String name;

    /**
     * 记录日期
     */
    private Date recordDate;

    public UtilityRecord(Integer houseId, String houseName, String name, Date recordDate) {
        this.houseId = houseId;
        this.houseName = houseName;
        this.name = name;
        this.recordDate = recordDate;
    }
}
