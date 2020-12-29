package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Menu;

@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

}
