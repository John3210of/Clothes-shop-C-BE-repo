package com.shop.mall.controller;

import com.shop.mall.dto.*;
import com.shop.mall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/regist") //1번 api
    public String memberRegist(@RequestBody MemberRegistRequestDto dto){
        return memberService.memberRegist(dto);
    }

    @PostMapping("/members/login") //2번 api
    public MemberLoginResponseDto memberLogin(@RequestBody MemberLoginRequestDto dto){
        return memberService.memberLogin(dto);
    }

    @GetMapping("/members/info") //3번 api
    public MemberInfoResponseDto memberInfo(@RequestHeader(value = "nickname") String nickname){
        return memberService.memberInfo(nickname);
    }

    @PostMapping("/members/cash") //4번 api
    public MemberCashResponseDto cashCharge(@RequestHeader(value = "nickname") String nickname,
                                            @RequestBody MemberCashRequestDto dto){
        return memberService.cashCharge(nickname,dto.getChargeCash());
    }

    @PatchMapping("/members/address") //5번 api
    public MemberAddressResponseDto addressChange(@RequestHeader(value = "nickname") String nickname,
                                                  @RequestBody MemberAddressRequestDto dto){
        return memberService.addressChange(nickname,dto.getAfterAddress());
    }

    @PatchMapping("/members/nickname") //6번 api
    public MemberNameResponseDto nameChange(@RequestHeader(value = "nickname") String nickname,
                                                  @RequestBody MemberNameRequestDto dto){
        return memberService.nameChange(nickname,dto.getAfterNickname());
    }
}
