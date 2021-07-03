package com.sparta.springcore.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//스프링 시큐리티가 기본 로그인 포맷을 지원함!! 회원가입 페이지는 없음.
// id = user
// pw = 콘솔에 뜬 28자 security password

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함. 위 두개 @은 필수.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                //image폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
                //css폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                //회원과 관련된 처리를 할 때(회원가입 등)는 login 없이 허용. 안 하면 회원가입 페이지 이동 안 됨.
                .antMatchers("/user/**").permitAll()
                //h2 콘솔도 허용 없이 접근 가능하도록 설정.
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated() //어떤 요청이 오든지 로그인 과정이 없으면 로그인 하도록 한다
                //위 andMatchers가 없으면 img와 CSS 파일에 대해서도 인증을 요구하기 때문에, CSS가 적용 안 됨.
                .and()
                .formLogin()
                //적어도 로그인 페이지에서는 인증을 요구하지 않는다. 로그인 시켜야되니까.
                .loginPage("/user/login") // 로그인이 필요할 때 필요한 페이지의 위치 지정
                .failureUrl("/user/login/error") // 로그인이 실패했을 때 어디로 이동할것인가
                .defaultSuccessUrl("/") //로그인 후 이동할 위치
                .permitAll()
                .and()
                .logout() // 로그아웃 기능도 허용!
                .permitAll();
    }
}