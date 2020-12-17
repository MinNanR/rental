package site.minnan.rental.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录后返回的信息
 * @author Minnan on 2020/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    private String token;

}
