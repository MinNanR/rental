package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.Room;
import site.minnan.rental.domain.vo.RoomDropDown;
import site.minnan.rental.domain.vo.UtilityInitVO;

import java.util.List;

/**
 * @author Minnan on 2020/12/29
 */
@Mapper
@Repository
public interface RoomMapper extends BaseMapper<Room> {

    @Select("select id from rental_room where room_number = #{roomNumber} and house_id = #{houseId} limit 1")
    Integer checkRoomNumberUsed(@Param("houseId") Integer houseId, @Param("roomNumber") String roomNumber);

    @Select("select id from rental_room where id = #{id} limit 1")
    Integer checkRoomExists(@Param("id") Integer id);

    @Select("select id id, room_number roomNumber from rental_room where house_id = #{houseId}")
    List<RoomDropDown> getRoomDropDown(@Param("houseId") Integer houseId);

    Integer updateRoomStatusBatch(@Param("roomList")List<Room> roomList);

    @Select("select distinct floor from rental_room where house_id = #{houseId}")
    List<Integer> getFloorDropDown(@Param("houseId") Integer houseId);

    List<UtilityInitVO> getRoomList(@Param("houseId")Integer houseId, @Param("floor")Integer floor,
                                    @Param("status") String status);
}
