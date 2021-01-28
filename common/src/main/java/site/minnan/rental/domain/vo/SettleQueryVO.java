package site.minnan.rental.domain.vo;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算账单时水点返回参数
 * @author Minnan on 2021/1/22
 */
@Data
@AllArgsConstructor
public class SettleQueryVO implements Serializable {

    private JSONObject utilityStart;

    private JSONObject utilityEnd;
}
