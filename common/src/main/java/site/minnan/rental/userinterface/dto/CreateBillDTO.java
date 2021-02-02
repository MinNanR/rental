package site.minnan.rental.userinterface.dto;

import cn.hutool.json.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 房间状态由空闲转为在租时创建账单参数
 * @author Minnan on 2021/01/07
 */
@Data
@Builder
public class CreateBillDTO implements Serializable {

    private Integer roomId;

    private Integer deposit;

    private Integer cardQuantity;

    private String remark;

    private String checkInDate;

    private String payMethod;

    private List<Integer> tenantIdList;

    private Integer userId;

    private String userName;
}
