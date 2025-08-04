package com.promesa.promesa.common.validator;

import com.promesa.promesa.domain.artist.exception.ForbiddenArtistAccessException;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.inquiry.domain.Inquiry;
import com.promesa.promesa.domain.inquiry.exception.ForbiddenInquiryAccessException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.domain.Role;

public class PermissionValidator {
    public static void validateCanModifyArtist(Artist artist, Member member) {
        boolean isAdmin = member.hasRole(Role.ROLE_ADMIN);
        boolean isOwner = artist.getMember().getId().equals(member.getId());

        if (!isAdmin && !isOwner) {
            throw ForbiddenArtistAccessException.EXCEPTION;
        }
    }

    public static void validateCanModifyInquiry(Inquiry inquiry, Member member) {
        boolean isAdmin = member.hasRole(Role.ROLE_ADMIN);
        boolean isOwner = inquiry.getArtist().getMember().getId().equals(member.getId());

        if (!isAdmin && !isOwner) {
            throw ForbiddenInquiryAccessException.EXCEPTION;
        }
    }
}
