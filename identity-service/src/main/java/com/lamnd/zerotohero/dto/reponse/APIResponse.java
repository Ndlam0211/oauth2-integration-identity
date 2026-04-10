package com.lamnd.zerotohero.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private int code = 200;
    private String message;
    private T data;
}
