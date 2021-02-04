package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.House;

import java.util.List;

/**
 * @author Minnan on 2020/12/17
 */
@Repository
@Mapper
public interface HouseMapper extends BaseMapper<House> {

    @Select("select id id, house_name houseName from rental_house")
    List<House> getHouseDropDown();
}
