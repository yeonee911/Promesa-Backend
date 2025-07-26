package com.promesa.promesa.security.jwt;

import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.security.jwt.exception.InvalidTokenFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String subject) {
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw InvalidTokenFormatException.EXCEPTION;
        }

        String provider = parts[0];
        String providerId = parts[1];

        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        // 탈퇴한 사용자라면 로그인 거부
        if (Boolean.TRUE.equals(member.getIsDeleted())) {
            throw new DisabledException("탈퇴 처리된 사용자입니다.");
        }

        return new CustomUserDetails(member);
    }


}
