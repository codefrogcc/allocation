package org.apache.commons.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;


@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        Set<String> uris = new HashSet<String>();
        //uris.add("/index/login");
        //uris.add("/index/getVerify");
        //uris.add("/index/adminLogin");

        //String requestURI = request.getRequestURI();
        String servletPath = request.getServletPath();
        //StringBuffer requestURL = request.getRequestURL();
        String contextPath = request.getContextPath();

        //System.out.println("requestURI="+requestURI); //    /atcrowdfunding-main/jquery/jquery-2.1.1.min.js
        //System.out.println("servletPath=" + servletPath);//     /jquery/jquery-2.1.1.min.js
        //System.out.println("requestURL="+requestURL); //    http://localhost/atcrowdfunding-main/jquery/jquery-2.1.1.min.js
        //System.out.println("contextPath="+contextPath); //  /atcrowdfunding-main

        if (uris.contains(servletPath)) {
            return true;
        } else {
            //true : 表示一定获取session对象.
            //如果之前服务器给客户端分配过session,则获取这个session,否则,创建一个新的.
            //false : 表示获取之前分配过的session
            //如果之前服务器给客户端分配过session,则获取这个session,否则,返回null.
            HttpSession session = request.getSession(true);
            Object user = session.getAttribute("adminAccount");

            if (user == null) {
                response.sendRedirect(contextPath + "/admin");
                return false;
            } else {
                return true;
            }
        }
    }
}
