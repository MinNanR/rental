package site.minnan.rental.infrastructure.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import site.minnan.rental.infrastructure.security.JwtRequestFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * SpringSecurity配置（仅验证）
 * created by Minnan on 2020/12/16
 */
@Component
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
                    log.error("登录异常", ex);
                    if(ex instanceof InsufficientAuthenticationException){
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录信息缺失");
                    }else{
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    }
                })
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //对于在header里面增加token等类似情况，放行所有OPTIONS请求。
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        super.configure(web);
    }
}
