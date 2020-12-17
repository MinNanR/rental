package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 添加房屋参数
 * @author Minnan on 2020/12/17
 */
@Data
public class AddHouseDTO {

    @NotEmpty
    private String address;

    @NotEmpty
    private String directorName;

    @NotEmpty
    @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式不正确")
    private String directorPhone;
}
