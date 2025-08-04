package com.promesa.promesa.domain.artist.exception;

import com.promesa.promesa.common.exception.GlobalErrorCode;
import com.promesa.promesa.common.exception.PromesaCodeException;

public class ForbiddenArtistAccessException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ForbiddenArtistAccessException();
    private ForbiddenArtistAccessException() {
        super(ArtistErrorCode.FORBIDDEN_ARTIST_ACCESS);
    }
}