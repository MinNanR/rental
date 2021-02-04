package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 更新房屋时的参数
 * @author Minnan on 2020/12/21
 */
@Data
public class UpdateHouseDTO {

    @NotNull(message = "未指定更新的对象")
    private Integer id;

    private String houseName;

    private String address;

    private String directorName;

    @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式不正确")
    private String directorPhone;

    private String longitude;

    private String latitude;


}
