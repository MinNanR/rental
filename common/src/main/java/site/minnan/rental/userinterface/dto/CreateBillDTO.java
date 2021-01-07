package site.minnan.rental.userinterface.dto;

import cn.hutool.json.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 房间状态由空闲转为在租时创建账单参数
 * @author Minnan on 2021/01/07
 */
@Data
@Builder
public class CreateBillDTO implements Serializable {


    private Integer roomId;

    private Integer userId;

    private String userName;

    private JSONObject roomInfo;
}