package com.ensemble.auth_server.exception;

import com.ensemble.auth_server.dto.ErrorResponseDto;
import com.ensemble.auth_server.dto.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseSuccessAdvice implements ResponseBodyAdvice<Object> {

    private static final String JWKS_PATH = "/.well-known/jwks.json";

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // ✅ IMPORTANT: Do NOT wrap JWKS response (must be {"keys":[...]})
        String path = request.getURI().getPath();
        if (JWKS_PATH.equals(path)) {
            return body;
        }

        // ❌ Skip if already wrapped
        if (body instanceof ResponseDto<?> || body instanceof ErrorResponseDto) {
            return body;
        }

        // ✅ If ResponseEntity, wrap its body (avoid double wrap)
        if (body instanceof ResponseEntity<?> entity) {
            Object actualBody = entity.getBody();

            if (actualBody instanceof ResponseDto<?> || actualBody instanceof ErrorResponseDto) {
                return body;
            }

            // keep original status code if you want
            return ResponseEntity.status(entity.getStatusCode()).body(successResponse(actualBody));
        }

        // ✅ Wrap normal response
        return successResponse(body);
    }

    private <T> ResponseDto<T> successResponse(T body) {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.setStatusCode(HttpStatus.OK.value());
        dto.setStatusMsg("Success");
        dto.setData(body);
        return dto;
    }
}
