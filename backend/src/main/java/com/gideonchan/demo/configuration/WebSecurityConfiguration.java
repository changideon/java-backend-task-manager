package com.gideonchan.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                );

                http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/login")
                        .ignoringRequestMatchers("/api/todo")
                        .ignoringRequestMatchers("/api/todo/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String username = "user";
        String password = "password";
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        String encodedPassword = passwordEncoder().encode(password);
        manager.createUser(User.withUsername(username).password(encodedPassword).roles("USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
