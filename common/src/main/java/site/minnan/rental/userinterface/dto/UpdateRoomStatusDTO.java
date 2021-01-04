package site.minnan.rental.userinterface.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改房间状态参数
 * @author Minnan on 2020/12/29
 */
@Data
@AllArgsConstructor
@Builder
public class UpdateRoomStatusDTO implements Serializable {

    private Integer id;

    private String status;

    private Integer userId;

    private String userName;
}
