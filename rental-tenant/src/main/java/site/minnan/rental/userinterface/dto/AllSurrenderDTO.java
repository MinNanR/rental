package site.minnan.rental.userinterface.dto;

import lombok.Data;

import java.util.List;

/**
 * 房客全部退租参数
 * @author Minnan on 2021/1/28
 */
@Data
public class AllSurrenderDTO {

    List<Integer> idList;
}
