package com.cineflex.api.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

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
            .requestMatchers(HttpMethod.DELETE, "/api/comments/*").hasAuthority("MODERATE_COMMENT")
            .requestMatchers(HttpMethod.DELETE, "/api/episodes/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.DELETE, "/api/seasons/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.DELETE, "/api/shows/*").hasAuthority("MUTATE_CONTENT")

            .requestMatchers(HttpMethod.GET, "/api/authentication/profile").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/authentication/role").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/comments/sections/*/deleted").hasAuthority("MODERATE_COMMENT")
            .requestMatchers(HttpMethod.GET, "/api/episodes/liked").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/favorites").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/hirers/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.GET, "/api/hirers").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.GET, "/api/shows/*/is-favorite").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/shows/*/rate").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/users/subscription").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/users/view-history/*").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/users/view-history").authenticated()

            .requestMatchers(HttpMethod.POST, "/api/advertisements").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.POST, "/api/comments/report/*/approve").hasAuthority("MODERATE_COMMENT")
            .requestMatchers(HttpMethod.POST, "/api/comments/report/*/ignore").hasAuthority("MODERATE_COMMENT")
            .requestMatchers(HttpMethod.POST, "/api/comments/sections/*").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/comments/*/report").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/episodes/*/comments").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/episodes/*/like").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/episodes/*/unlike").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/episodes/*/view-history").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/genres").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.POST, "/api/hirers").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/seasons/*/episodes").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.POST, "/api/shows/*/comments").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/shows/*/favorite").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/shows/*/rate").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/shows/*/seasons").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.POST, "/api/shows/*/unfavorite").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/shows").hasAuthority("MUTATE_CONTENT")
            
            .requestMatchers(HttpMethod.PUT, "/api/advertisements/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/episodes/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/hirers/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/seasons/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/shows/*").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/shows/*/genres").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/users/*/ban").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/users/*/unban").hasAuthority("MUTATE_CONTENT")
            .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAuthority("MUTATE_CONTENT")

            .anyRequest().permitAll()
        );
        
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5500",
            "http://localhost:5173",
            "https://cineflexz.netlify.app",
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("X-Total-Page"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
