package site.minnan.rental.userinterface.dto;

import lombok.Data;

@Data
public class GetHouseListDTO extends ListQueryDTO {

    private String address;
}
