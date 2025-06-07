package com.example.ticket_platform.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.ticket_platform.component.CustomAccessDeniedHandler;
import com.example.ticket_platform.component.CustomAuthenticationEntryPoint;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
        private final CustomAccessDeniedHandler customAccessDeniedHandler;

        public SecurityConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                        CustomAccessDeniedHandler customAccessDeniedHandler) {
                this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
                this.customAccessDeniedHandler = customAccessDeniedHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/webjars/**", "/css/**", "/js/**", "/imgs/**")
                                                .permitAll()
                                                .requestMatchers("/login", "/not_autenticated", "/logout", "/",
                                                                "/API/**")
                                                .permitAll()
                                                .requestMatchers("/operatori", "/addTicket", "/editTicket/**",
                                                                "/deleteTicket/**", "/createUser", "/clienti", "/API",
                                                                "/addAPI", "/addCliente", "/editCliente/**")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers("/permissions_missing", "/addNote/**")
                                                .hasAnyAuthority("USER", "ADMIN")
                                                .requestMatchers("/index", "/editUser/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/editTicket/**", "/addTicket",
                                                                "/deleteTicket/**",
                                                                "/editUser/**", "/createUser", "/deleteCliente/**",
                                                                "/deleteNote/**", "/addCliente", "/editCliente/**")
                                                .hasAnyAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/addNote/**")
                                                .hasAnyAuthority("USER")
                                                .requestMatchers(HttpMethod.POST, "/addNote/**", "/editUser/**")
                                                .hasAnyAuthority("USER", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/index", true)
                                                .permitAll())
                                .logout(logout -> logout.invalidateHttpSession(true).deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                                .accessDeniedHandler(customAccessDeniedHandler))
                                .sessionManagement(session -> session
                                                .maximumSessions(1)
                                                .expiredUrl("/login?logout")
                                                .sessionRegistry(sessionRegistry()));
                ;
                return http.build();
        }

        @Bean
        public SessionRegistry sessionRegistry() {
                return new SessionRegistryImpl();
        }

        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

        @Bean
        public CustomJdbcUserDetailsManager customJdbcUserDetailsManager(DataSource dataSource,
                        UserRepository userRepository,
                        AuthoritiesRepository authoritiesRepository) {
                return new CustomJdbcUserDetailsManager(dataSource, userRepository, authoritiesRepository);
        }

        @Bean
        @SuppressWarnings("deprecation")
        PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }

}
