package site.minnan.rental.userinterface.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 更新房客信息参数
 *
 * @author Minnan on 2021/01/04
 */
@Data
public class UpdateTenantDTO {

    @NotNull(message = "未指定要修改的房客")
    private Integer id;

    private String name;

    private String gender;

    @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
            message = "身份证号码格式不正确")
    private String identificationNumber;

    private String hometownProvince;

    private String hometownCity;
}
