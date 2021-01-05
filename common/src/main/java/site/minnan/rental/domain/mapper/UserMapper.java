package site.minnan.rental.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import site.minnan.rental.domain.aggregate.AuthUser;

import javax.websocket.server.PathParam;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<AuthUser> {

    @Select("select id from auth_user where username = #{username}")
    Integer checkUsernameUsed(@PathParam("username") String username);
}
