package com.lamnd.zerotohero.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IntrospectResponse {
    private boolean isValid;
}
