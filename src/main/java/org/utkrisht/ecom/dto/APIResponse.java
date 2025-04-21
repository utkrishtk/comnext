package org.utkrisht.ecom.dto;

import lombok.Data;

@Data
public class APIResponse {
    private String message;
    private boolean status;

    public APIResponse(String message, boolean b) {
        this.message = message;
        this.status = b;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
