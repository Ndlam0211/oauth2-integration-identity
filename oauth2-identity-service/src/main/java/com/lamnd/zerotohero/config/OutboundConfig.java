package com.lamnd.zerotohero.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OutboundConfig {
    @Value("${outbound.identity.client_id}")
    private String clientId;

    @Value("${outbound.identity.client_secret}")
    private String clientSecret;

    @Value("${outbound.identity.redirect_uri}")
    private String redirectUri;

    @Value("${outbound.identity.grant_type}")
    private String grantType;
}
