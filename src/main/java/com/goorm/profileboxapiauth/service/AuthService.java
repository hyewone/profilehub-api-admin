package com.goorm.profileboxapiauth.service;

import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.enumeration.ProviderType;
import com.goorm.profileboxcomm.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    @Transactional
    public Optional<Member> getMemberByMemberEmailAndProviderType(String memberEmail, ProviderType providerType){
        return authRepository.findAllByMemberEmailAndProviderType(memberEmail, providerType);
    }

    @Transactional
    public Member addMember(Member member){
        return authRepository.save(member);
    }
}