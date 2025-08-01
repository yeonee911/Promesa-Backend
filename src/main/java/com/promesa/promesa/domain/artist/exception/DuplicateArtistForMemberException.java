package com.promesa.promesa.domain.artist.exception;

import com.promesa.promesa.common.exception.PromesaCodeException;

public class DuplicateArtistForMemberException extends PromesaCodeException {
    public static final PromesaCodeException EXCEPTION = new DuplicateArtistForMemberException();

    private DuplicateArtistForMemberException() {
        super(ArtistErrorCode.ALREADY_ARTIST);
    }
}
