package site.minnan.rental.userinterface.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 启用用户参数（租客退租后重新租房时）
 * @author Minnan on 2021/01/05
 */
@Data
@Builder
public class EnableTenantUserBatchDTO implements Serializable {

    private List<Integer> userIdList;

    private Integer userId;

    private String userName;
}
