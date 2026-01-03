package com.ensemble.auth_server.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {

    private Integer statusCode;
    private String statusMsg;
    private T data;

}
