package com.cdivtc.interceptor;

import com.cdivtc.domain.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResourcesInterceptor extends HandlerInterceptorAdapter {
    //任意角色都能访问的路径
    private List<String> ignoreUrl;
    public ResourcesInterceptor(List<String> ignoreUrl){
        this.ignoreUrl = ignoreUrl;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        // 获取请求的路径
        String uri = request.getRequestURI();
        //对用户登录的相关请求放行
        if(uri.contains("login")){
            return true;
        }
        User user = (User) request.getSession().getAttribute("USER_SESSION");
        // 如果用户是已登录状态，判断访问的资源是否有权限
        if(user != null){
            //管理员放行
            if("ADMIN".equals(user.getRole())){
                return true;
            }else{//否则
                for(String url:ignoreUrl){
                    //访问的资源不是管理员权限的资源放行
                    if(uri.contains(url)){
                        return true;
                    }
                }
            }
        }

        // 其他情况都直接跳转到登录界面
        request.setAttribute("msg","您还没有登录，请先登录！");
        request.getRequestDispatcher("/admin/login.jsp").forward(request,response);
        return false;
    }
}
