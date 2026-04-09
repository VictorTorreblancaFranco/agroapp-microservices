package pe.agro.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import pe.agro.userservice.repository.UserRepository;
import pe.agro.userservice.security.JwtAuthenticationFilter;
import pe.agro.userservice.security.JwtUtil;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtAuthenticationFilter jwtAuthenticationFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/users/save").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/change-role/**").hasRole("SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/delete/**", "/api/v1/users/restore/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/status/inactive", "/api/v1/users/status/active").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users", "/api/v1/users/active", "/api/v1/users/active/verified").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/email/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/{id}").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/check-email/**", "/api/v1/users/check-phone/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .pathMatchers("/api/v1/users/me").hasAnyRole("USER", "MANAGER", "ADMIN", "SUPER_ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .cast(org.springframework.security.core.userdetails.UserDetails.class)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found: " + username)));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil,
                                                           ReactiveUserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }
}