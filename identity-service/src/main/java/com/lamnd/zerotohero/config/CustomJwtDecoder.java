package com.lamnd.zerotohero.config;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.lamnd.zerotohero.repository.BlacklistTokenRepo;
import com.lamnd.zerotohero.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtConfig jwtConfig;
    private final JwtUtil jwtUtil;
    private final BlacklistTokenRepo blacklistTokenRepo;

    private NimbusJwtDecoder jwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (blacklistTokenRepo.existsById(jwtUtil.getClaimsSet(token).getJWTID())) {
                throw new JwtException("Token is locked");
            }
        } catch (ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(jwtDecoder)) {
            SecretKeySpec spec = new SecretKeySpec(jwtConfig.getSecretKey().getBytes(), "HS512");

            jwtDecoder = NimbusJwtDecoder.withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return jwtDecoder.decode(token);
    }
}
