package com.goorm.profileboxapiadmin.controller;

import com.goorm.profileboxapiadmin.auth.PrincipalDetails;
import com.goorm.profileboxapiadmin.service.MemberService;
import com.goorm.profileboxcomm.dto.member.MemberDTO;
import com.goorm.profileboxcomm.dto.member.request.CreateMemberRequestDTO;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.response.ApiResult;
import com.goorm.profileboxcomm.response.ApiResultType;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/test")
    public String test() {
        return "테스트 하기!! - 2";
    }
    // 회원 전체 리스트 출력
    @GetMapping("/member/all")
    public ApiResult<List<Member>> all() {
        List<Member> list = memberService.testListAllMember();
        return ApiResult.getResult(ApiResultType.SUCCESS, "테스트 - 회원 전체 리스트 출력", list);
    }

    // 회원 가입
    @PostMapping("/member")
    public ApiResult<MemberDTO> registerMemberController(@Valid @RequestBody CreateMemberRequestDTO dto) {
        MemberDTO registeredMemberDTO = memberService.saveMember(dto);
        return ApiResult.getResult(ApiResultType.SUCCESS, "회원가입", registeredMemberDTO, HttpStatus.CREATED);
    }

    // 회원 탈퇴
    @DeleteMapping("/member")
    public ApiResult withdrawalMemberController(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.deleteMember(principalDetails.getMember().getMemberId());
        return ApiResult.getResult(ApiResultType.SUCCESS, "회원탈퇴", null);
    }

    // 회원 조회 (다른 BE API 서버에서도 호출하는 함수)
    @GetMapping("/member/email/{email}")
    public ApiResult<MemberDTO> getMemberInfoController(@PathVariable("email") String email){
        MemberDTO member = memberService.findLoginMemberByEmailToDto(email);
        return ApiResult.getResult(ApiResultType.SUCCESS, "로그인", member);
    }

    @GetMapping("/member/memberId/{memberId}")
    public ApiResult<MemberDTO> getMemberInfoController(@PathVariable("memberId") Long memberId){
        MemberDTO member = memberService.findMemberByMemberId(memberId);
        return ApiResult.getResult(ApiResultType.SUCCESS, "member return", member);
    }

}
