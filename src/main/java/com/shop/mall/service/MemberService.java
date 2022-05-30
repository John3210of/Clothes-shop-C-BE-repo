package com.shop.mall.service;

import com.shop.mall.domain.Member;
import com.shop.mall.dto.*;
import com.shop.mall.exception.ErrorCodeException;
import com.shop.mall.repository.MemberRepository;
import com.shop.mall.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shop.mall.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public String memberRegist(MemberRequestDto.Regist dto) {
//        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
//            throw new ErrorCodeException(PW_NOT_MATCH_PWCHECK);
//        }
//        if (memberRepository.existsByEmailOrNickname(dto.getEmail(), dto.getNickname())) {
//            throw new ErrorCodeException(USERNAME_DUPLICATE);
//        }
        memberValidator.registerDto(dto);

        Member member = new Member(dto.getEmail(),
                dto.getNickname(),
                dto.getAddress(),
                dto.getPassword(),
                0);
        memberRepository.save(member);
        return "msg : 회원가입 완료";
    }

    //로그인
    public MemberResponseDto.Login memberLogin(MemberRequestDto.Login dto) {
        Member member = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword()).orElseThrow(
                () -> new ErrorCodeException(LOGIN_NOT_MATCH));
        return new MemberResponseDto.Login(member.getNickname());
    }

    //정보 보기
    public MemberResponseDto.Info memberInfo(String nickname) {
        Member member = memberValidator.authorization(nickname);
        return new MemberResponseDto.Info(member.getNickname(),member.getAddress(),member.getCash());
//        return new MemberResponseDto.Info(memberValidator.authorization(nickname).getNickname(),
//                memberValidator.authorization(nickname).getAddress(), memberValidator.authorization(nickname).getCash());
    }

    @Transactional
    public MemberResponseDto.Cash cashCharge(String nickname, int chargeCash) {
        int totalCash = memberValidator.authorization(nickname).charge(chargeCash);
        return new MemberResponseDto.Cash(totalCash);
    }

    @Transactional
    public MemberResponseDto.Address addressChange(String nickname, String afterAddress) {
        memberValidator.authorization(nickname).addressUpdate(afterAddress);
        return new MemberResponseDto.Address(afterAddress);
    }

    @Transactional
    public MemberResponseDto.Name nameChange(String nickname, String afterName) {
        if (memberRepository.existsByNickname(afterName)){
            throw new ErrorCodeException(USERNAME_DUPLICATE2);
        }
        memberValidator.authorization(nickname).nameUpdate(afterName);
        return new MemberResponseDto.Name(afterName);
    }

    @Transactional
    public String memberDelete(String nickname) {
        memberRepository.deleteByNickname(nickname).orElseThrow(
                () -> new ErrorCodeException(MEMBER_NOT_EXIST)
        );
        return "msg : 회원 탈퇴 완료";
    }
}
