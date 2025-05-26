package com.promesa.promesa.domain.member.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class MemberNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new MemberNotFoundException();
    private MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
