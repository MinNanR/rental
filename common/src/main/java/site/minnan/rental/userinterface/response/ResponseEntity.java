package site.minnan.rental.userinterface.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T> {

    private String code;

    private String message;

    private T data;

    public ResponseEntity(ResponseCode responseCode){
        this.code = responseCode.code();
        this.message = responseCode.message();
    }

    public static ResponseEntity<?> success(){
        return new ResponseEntity<>(ResponseCode.SUCCESS);
    }

    public static<T> ResponseEntity<T> success(T data){
        ResponseEntity<T> responseEntity = new ResponseEntity<>(ResponseCode.SUCCESS);
        responseEntity.setData(data);
        return responseEntity;
    }

    public static<T> ResponseEntity<T> message(String message){
        ResponseEntity<T> responseEntity = new ResponseEntity<>(ResponseCode.SUCCESS);
        responseEntity.message = message;
        return responseEntity;
    }

    public static ResponseEntity<?> fail(ResponseCode responseCode){
        return new ResponseEntity<>(responseCode);
    }

    public static<T> ResponseEntity<T> fail(ResponseCode responseCode, T data){
        ResponseEntity<T> responseEntity = new ResponseEntity<>(responseCode);
        responseEntity.setData(data);
        return responseEntity;
    }

    public static<T> ResponseEntity<T> fail(String message){
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        ResponseCode responseCode = ResponseCode.FAIL;
        responseEntity.code = responseCode.code();
        responseEntity.message = message;
        return responseEntity;
    }
}
