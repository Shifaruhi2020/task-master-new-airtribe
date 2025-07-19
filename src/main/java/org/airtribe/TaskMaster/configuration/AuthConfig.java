package org.airtribe.TaskMaster.configuration;

import org.airtribe.TaskMaster.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class AuthConfig {

    @Autowired
    private LogoutService logoutService; 

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SecurityFilterChain securityFilterChainAuth(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests.requestMatchers("/register",
             "verifyRegistration",
              "signin",
               "/tokenHello", 
               "/hello",
                "/tasks/create",
                "/tasks/my-tasks",
                "/tasks/{taskId}/complete",
                "/tasks/{taskId}/assign/{assigneeId}",
                "/tasks/filter",
                "/tasks/search",
                "tasks/{taskId}/comments",
                "tasks/{taskId}/comments",
                "/tasks/{taskId}/attachments",
                "/tasks/{taskId}/attachments",
                "/project/create",
                "/project/{projectId}",
                "/auth/logout",
                "/project/{projectId}/invite/{userId}",
                "/project/{projectId}/members")
                .permitAll()
                .anyRequest()
                .authenticated())
        .formLogin(formLogin -> formLogin.defaultSuccessUrl("/hello", true).permitAll())
        .addFilterBefore(new JwtFilter(logoutService), UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
    }
    
    }
