package models.ws;

import com.google.gson.annotations.Expose;

public class PolicyProductResponse extends BaseResponse {

    @Expose
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
