package com.goorm.profileboxapiadmin.service;

import com.goorm.profileboxcomm.dto.member.MemberDTO;
import com.goorm.profileboxcomm.dto.member.request.CreateMemberRequestDTO;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // test용 멤머 모두 출력
    @Transactional
    public List<Member> testListAllMember(){
        return memberRepository.findAll();
    }

    public Boolean isAlready(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }

    @Transactional
    public MemberDTO saveMember(CreateMemberRequestDTO dto) {
        if(isAlready(dto.getMemberEmail())) throw new ApiException(ExceptionEnum.MEMBER_ALREADY_EXIST);
        //dto.setMemberPassword(passwordEncoder.encode(dto.getMemberPassword()));
        Member entity = CreateMemberRequestDTO.toEntity(dto);
        System.out.println("*********회원가입할 " + entity);
        entity.setMemberPassword(passwordEncoder.encode(entity.getMemberPassword()));
        MemberDTO memberDTO = Member.toDTO(memberRepository.save(entity));
        return memberDTO;
    }

    @Transactional
    public Member findLoginMemberByEmail(String email) {
        Member member =  memberRepository.findMemberByMemberEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN_FAILED));
        return member;
    }

    @Transactional
    public MemberDTO findLoginMemberByEmailToDto(String email) {
        Member member =  memberRepository.findMemberByMemberEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN_FAILED));

        MemberDTO memberDTO = Member.toDTO(member);
        return memberDTO;
    }


    @Transactional
    public MemberDTO findMemberByMemberId(Long memberId) {
        Member member =  memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN_FAILED));
        MemberDTO memberDTO = Member.toDTO(member);
        return memberDTO;
    }

    @Transactional
    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }
    private void validateDuplicateMember(Member entity){
        if(entity == null ){
            log.warn("entity is null!");
            throw new RuntimeException("Invalid arguments!");
        }
        String email = entity.getMemberEmail();
        if(memberRepository.existsByMemberEmail(email)){
            log.warn("Email already exist!");
            throw new RuntimeException("Email already exist!");
        }
    }
}
