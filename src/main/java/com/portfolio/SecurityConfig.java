package com.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Clients - ADVISOR full access, CLIENT read only
                        .requestMatchers(HttpMethod.GET, "/api/clients/**").hasAnyRole("ADVISOR", "CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/clients/**").hasRole("ADVISOR")
                        .requestMatchers(HttpMethod.PUT, "/api/clients/**").hasRole("ADVISOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/clients/**").hasRole("ADVISOR")

                        // Portfolios - ADVISOR full access, CLIENT read only
                        .requestMatchers(HttpMethod.GET, "/api/portfolios/**").hasAnyRole("ADVISOR", "CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/portfolios/**").hasRole("ADVISOR")
                        .requestMatchers(HttpMethod.PUT, "/api/portfolios/**").hasRole("ADVISOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/portfolios/**").hasRole("ADVISOR")

                        // Transactions - ADVISOR only
                        .requestMatchers("/api/transactions/**").hasRole("ADVISOR")

                        // Holdings - both can view
                        .requestMatchers("/api/holdings/**").hasAnyRole("ADVISOR", "CLIENT")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}