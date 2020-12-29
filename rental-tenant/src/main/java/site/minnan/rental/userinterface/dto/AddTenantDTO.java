package site.minnan.rental.userinterface.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import site.minnan.rental.infrastructure.enumerate.Gender;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 添加房客参数
 *
 * @author Minnan on 2020/12/28
 */
@Data
public class AddTenantDTO {

    @NotEmpty(message = "房客姓名不能为空")
    private String name;

    @NotEmpty(message = "性别未填写")
    private String gender;

    @NotEmpty
    @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @NotNull
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
            message = "身份证号码格式不正确")
    private String identificationNumber;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC+8:00")
    private Date birthday;

    private String hometownProvince;

    private String hometownCity;

    @NotNull(message = "未指定房间")
    private Integer roomId;

    private String roomNumber;
}
