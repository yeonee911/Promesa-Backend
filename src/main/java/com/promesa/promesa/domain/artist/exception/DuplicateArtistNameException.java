package com.promesa.promesa.domain.artist.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DuplicateArtistNameException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DuplicateArtistNameException();

    private DuplicateArtistNameException() {
        super(ArtistErrorCode.DUPLICATE_ARTIST_NAME);
    }
}
