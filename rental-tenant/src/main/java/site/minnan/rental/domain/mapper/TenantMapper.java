package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Tenant;

import java.util.Collection;
import java.util.List;

@Repository
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    @Select("select id from rental_tenant where identification_number = #{identificationNumber} limit 1")
    Integer checkTenantExistByIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Select("select id from rental_tenant where room_id = #{roomId} and status = 'LIVING' limit 1")
    Integer checkRoomOnRentByRoomId(@Param("roomId") Integer roomId);

    /**
     * 批量添加房客
     *
     * @param tenantList
     */
    void addTenantBatch(@Param("list") List<Tenant> tenantList);

    /**
     * 批量根据房间id获取房客名称
     *
     * @param ids
     * @return
     */
    List<Tenant> getTenantByRoomIds(@Param("ids") Collection<Integer> ids);
}
