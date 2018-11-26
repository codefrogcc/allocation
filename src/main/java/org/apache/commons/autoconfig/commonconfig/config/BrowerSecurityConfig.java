package org.apache.commons.autoconfig.commonconfig.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated();//任何用户需要权限校验


        //开启自动配置的注销功能；默认访问/logout 表示用户注销，清空session
        http.logout().logoutSuccessUrl("/gotologin");//注销成功以后来到的页面路径

        //开启自动配置登录功能，去往登录页面路径“/gotologin”,登录路径“/userLogin”
        http.formLogin().loginPage("/gotologin").loginProcessingUrl("/userLogin");

        //开启记住我功能
        //http.rememberMe().rememberMeParameter("remeber");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //校验注入Service
        //auth.userDetailsService();
    }
}
