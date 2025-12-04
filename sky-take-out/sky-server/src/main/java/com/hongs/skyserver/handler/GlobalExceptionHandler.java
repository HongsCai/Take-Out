package com.hongs.skyserver.handler;

import com.hongs.skycommon.exception.BaseException;
import com.hongs.skyserver.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public BaseResponse handleException(BaseException e) {
        log.error("异常信息：{}", e.getMessage());
        return BaseResponse.error(e.getMessage());
    }
}
