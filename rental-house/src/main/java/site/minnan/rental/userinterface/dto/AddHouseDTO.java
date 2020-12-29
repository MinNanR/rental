package site.minnan.rental.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 添加房屋参数
 *
 * @author Minnan on 2020/12/17
 */
@Data
public class AddHouseDTO {

    @NotEmpty(message = "房屋名称不能为空")
    private String houseName;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "管理员不能为空")
    private String directorName;

    @NotEmpty(message = "管理员联系电话不能为空")
    @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式不正确")
    private String directorPhone;

    private String longitude;

    private String latitude;
}
