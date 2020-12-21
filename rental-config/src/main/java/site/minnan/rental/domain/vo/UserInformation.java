package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 用户信息
 * @author Minnan on 2020/12/21
 */
@Data
@AllArgsConstructor
@Builder
public class UserInformation {

    private String role;

    private String realName;
}
