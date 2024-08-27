package com.example.BookHealthServiceOnline.Response;

public class GetTokenResponse {
    private String token;

    private long expiresIn;


    public GetTokenResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    // Getters and setters...
}