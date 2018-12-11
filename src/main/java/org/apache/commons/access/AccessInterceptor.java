package org.apache.commons.access;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.util.RedisKey;
import org.apache.commons.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;

@Component
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(handler instanceof HandlerMethod) {
            String telphone = request.getParameter("telphone");
            String type = request.getParameter("type");
            String deployType = request.getParameter("deployType");
            if(StringUtils.isBlank(type)){
                return true;
            }
            HandlerMethod hm = (HandlerMethod)handler;
            Method method = hm.getMethod();
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }

            String key = request.getRequestURI()+"/"+method.getName()+"/";
            if(StringUtils.isBlank(deployType)){
                if("1".equals(type)){
                    key += RedisKey.SUB_DEPLOY;
                }else
                if("2".equals(type)){
                    key += RedisKey.ADD_DEPOSIT;
                }else {
                    return true;
                }
            }else{
                if("1".equals(deployType)){
                    key += RedisKey.DEPLOY;
                }else
                if("2".equals(deployType)){
                    key += RedisKey.ADD_DEPLOY;
                }else{
                    return true;
                }
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            if(needLogin) {
                if(StringUtils.isBlank(telphone)) {
                    render(response, "{\"code\":400,\"msg\":\"用户编号不存在\"}");
                    return false;
                }
                key += telphone;
            }else {
                //do nothing
            }
            log.info("key:"+key);
            RedisUtil redisUtil = new RedisUtil(redisTemplate);
            Integer increment = (Integer)redisUtil.get(key);
            if(increment  == null) {
                redisUtil.set(key,1,seconds);
            }else if(increment < maxCount) {
                redisUtil.incr(key,1);
            }else {
                render(response, "{\"code\":400,\"msg\":\""+seconds+"秒内不能重复访问"+maxCount+"次\"}");
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String str)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        //String str  = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
