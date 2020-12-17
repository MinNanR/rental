package site.minnan.rental.userinterface.dto;

import lombok.Data;

/**
 * 查询用户列表参数
 * @author Minnan on 2020/12/16
 */
@Data
public class GetUserListDTO extends ListQueryDTO {

    private String realName;

    private String phoneNumber;
}
