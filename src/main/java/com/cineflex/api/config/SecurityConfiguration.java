package com.cineflex.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cineflex.api.service.AccountDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final AccountDetailService accountDetailService;

    public SecurityConfiguration (
        JwtFilter jwtFilter,
        AccountDetailService accountDetailService
    ) {
        this.jwtFilter = jwtFilter;
        this.accountDetailService = accountDetailService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/authentication/login").permitAll()
            .requestMatchers("/api/authentication/register").permitAll()
            .requestMatchers("/api/authentication/profile").authenticated()

            .requestMatchers(HttpMethod.GET, "/*").permitAll()

            .anyRequest().permitAll()
        );
        
        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountDetailService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return daoAuthenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
