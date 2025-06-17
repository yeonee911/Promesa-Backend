package com.promesa.promesa.domain.artist.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;
import com.promesa.promesa.domain.example.exception.ExampleErrorCode;

public class ArtistNotFoundException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new ArtistNotFoundException();

    private ArtistNotFoundException() {
        super(ArtistErrorCode.ARTIST_ERROR_CODE);
    }
}
