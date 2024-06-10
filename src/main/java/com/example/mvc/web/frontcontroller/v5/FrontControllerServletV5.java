package com.example.mvc.web.frontcontroller.v5;

import com.example.mvc.web.frontcontroller.ModelView;
import com.example.mvc.web.frontcontroller.MyView;
import com.example.mvc.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.mvc.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.mvc.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.example.mvc.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.mvc.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.mvc.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.example.mvc.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import com.example.mvc.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();

        initHandlerAdapter();
    }

    private void initHandlerAdapter() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object handler = gethandler(req);

        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView modelView = adapter.handle(req, resp, handler);

        MyView mv = viewResolver(modelView);

        mv.render(modelView.getModel(), req, resp);
    }

    private static MyView viewResolver(ModelView mv) {
        return new MyView("/WEB-INF/views/" + mv.getViewName() + ".jsp");
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler = " + handler);
    }

    private Object gethandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);

        return handler;
    }
}
