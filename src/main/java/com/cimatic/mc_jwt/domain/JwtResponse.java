package com.cimatic.mc_jwt.domain;

import java.io.Serializable;

public class JwtResponse implements Serializable{
    private String correlationId;
    private boolean valid;
    private String token;
    
    public JwtResponse(String correlationId, boolean valid, String token) {
        this.correlationId = correlationId;
        this.valid = valid;
        this.token = token;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    

    
}
