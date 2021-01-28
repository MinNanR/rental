package site.minnan.rental.userinterface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量禁用用户
 *
 * @author Minnan on 2021/1/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDisableUserDTO {

    private Integer userId;

    private String userName;

    private List<Integer> idList;
}
