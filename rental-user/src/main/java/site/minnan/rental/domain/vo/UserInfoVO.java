package site.minnan.rental.domain.vo;

import lombok.Builder;
import lombok.Data;
import site.minnan.rental.domain.aggregate.AuthUser;

/**
 * 用户详情
 * @author Minnan on 2021/01/06
 */
@Data
@Builder
public class UserInfoVO {

    private Integer id;

    private String username;

    private String realName;

    private String phone;

    private String role;

    public static UserInfoVO assemble(AuthUser user){
        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .phone(user.getPhone())
                .role(user.getRole().getRoleName())
                .build();
    }
}
