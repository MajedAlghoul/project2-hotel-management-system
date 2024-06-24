package com.hms.Security;

import com.hms.dto.JWTAuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.RepresentationModel;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;


public class JwtAuthResource extends RepresentationModel<JWTAuthResponse>{

    private final JWTAuthResponse jwtAuthResponse;

    public JwtAuthResource(JWTAuthResponse jwtAuthResponse) {
        this.jwtAuthResponse = jwtAuthResponse;
    }

    public JWTAuthResponse getJWTAuthResponse() {
        return jwtAuthResponse;
    }
}
