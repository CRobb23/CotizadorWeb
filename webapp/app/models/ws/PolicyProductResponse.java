package models.ws;

import com.google.gson.annotations.Expose;

public class PolicyProductResponse extends BaseResponse {

    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Expose
    private String code;


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
