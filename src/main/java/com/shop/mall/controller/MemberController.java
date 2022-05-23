package com.shop.mall.controller;

import com.shop.mall.dto.MemberLoginRequestDto;
import com.shop.mall.dto.MemberLoginResponseDto;
import com.shop.mall.dto.MemberRegistRequestDto;
import com.shop.mall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
