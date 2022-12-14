package me.stirngray.studyolle.Config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/","/sign-up","/login","/check-email","/check-email-token",
                        "/email-login","/check-email-login","/login-link").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile").permitAll()
                .anyRequest().authenticated();
    }

    // static 폴더에 접근할 수 있게 하기위한 Config
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
