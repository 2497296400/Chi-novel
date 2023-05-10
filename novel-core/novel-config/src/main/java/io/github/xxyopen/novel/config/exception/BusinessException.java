package io.github.xxyopen.novel.config.exception;

import io.github.xxyopen.novel.common.constant.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends  RuntimeException {
    private final ErrorCodeEnum errorCodeEnum;

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage(),null,false,false);
        this.errorCodeEnum = errorCodeEnum;
    }
}
