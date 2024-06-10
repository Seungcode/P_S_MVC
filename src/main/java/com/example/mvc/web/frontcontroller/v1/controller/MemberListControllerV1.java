package com.example.mvc.web.frontcontroller.v1.controller;

import com.example.mvc.domain.member.Member;
import com.example.mvc.domain.member.MemberRepository;
import com.example.mvc.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListControllerV1 implements ControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();

        req.setAttribute("members", members);

        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewPath);
        requestDispatcher.forward(req, resp);
    }
}
