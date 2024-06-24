package com.hms.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class JWTAuthResponse extends RepresentationModel<JWTAuthResponse> {
    private String accessToken;
    private String tokenType = "Bearer";

    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
