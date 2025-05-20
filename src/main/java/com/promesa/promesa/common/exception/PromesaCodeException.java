package com.promesa.promesa.common.exception;

import com.promesa.promesa.common.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromesaCodeException extends RuntimeException {

    private BaseErrorCode errorCode;
    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }
}
