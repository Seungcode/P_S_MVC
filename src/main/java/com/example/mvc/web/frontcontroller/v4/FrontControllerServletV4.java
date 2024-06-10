package com.example.mvc.web.frontcontroller.v4;
import com.example.mvc.web.frontcontroller.ModelView;
import com.example.mvc.web.frontcontroller.MyView;
import com.example.mvc.web.frontcontroller.v3.ControllerV3;
import com.example.mvc.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.mvc.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.mvc.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.example.mvc.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.mvc.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.mvc.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        ControllerV4 controllerV4 = controllerMap.get(requestURI);

        if(controllerV4 == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(req);
        Map<String, Object> model = new HashMap<>();

        String viewName = controllerV4.process(paramMap, model);

        MyView myView = viewResolver(viewName);

        myView.render(model, req, resp);
    }

    private static MyView viewResolver(String mv) {
        return new MyView("/WEB-INF/views/" + mv + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
}
