package com.example.mvc.web.frontcontroller.v3.controller;

import com.example.mvc.domain.member.Member;
import com.example.mvc.domain.member.MemberRepository;
import com.example.mvc.web.frontcontroller.ModelView;
import com.example.mvc.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        Integer age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView modelView = new ModelView("save-result");
        modelView.getModel().put("member", member);
        return modelView;
    }
}
