package models.ws;

import com.google.gson.annotations.Expose;

public class QueryPersonDetailRequest {

    @Expose
    private String clientCode;
    @Expose
    private String clientCif;

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCif() {
        return clientCif;
    }

    public void setClientCif(String clientCif) {
        this.clientCif = clientCif;
    }
}
