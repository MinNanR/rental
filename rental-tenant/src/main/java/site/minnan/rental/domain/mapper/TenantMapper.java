package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.domain.vo.TenantDropDownVO;

import java.util.List;

@Repository
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    @Select("select id from rental_tenant where identification_number = #{identificationNumber} limit 1")
    Integer checkTenantExistByIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Select("select id from rental_tenant where room_id = #{roomId} limit 1")
    Integer checkRoomOnRentByRoomId(@Param("roomId") Integer roomId);
}
