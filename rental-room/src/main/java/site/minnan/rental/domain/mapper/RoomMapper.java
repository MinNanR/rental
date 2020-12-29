package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Room;

/**
 * @author Minnan on 2020/12/29
 */
@Mapper
@Repository
public interface RoomMapper extends BaseMapper<Room> {

    @Select("select id from rental_room where room_number = #{roomNumber} and house_id = #{houseId} limit 1")
    Integer checkRoomNumberUsed(@Param("houseId") Integer houseId,@Param("roomNumber") String roomNumber);

    @Select("select id from rental_room where id = #{id} limit 1")
    Integer checkRoomExists(@Param("id") Integer id);
}
