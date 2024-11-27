package backend.dev.setting.config;

import backend.dev.setting.jwt.JwtAccessDeniedHandler;
import backend.dev.setting.jwt.JwtAuthenticationEntryPoint;
import backend.dev.setting.jwt.JwtAuthenticationFilter;
import backend.dev.setting.jwt.JwtAuthenticationProvider;
import backend.dev.user.oauth.OAuth2AuthenticationSuccessHandler;
import backend.dev.user.service.OAuth2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OAuth2Service oAuth2Service;
    private final ObjectMapper objectMapper;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(request -> request.requestMatchers("/api/user/logout").hasRole("USER").anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth->oauth.successHandler(oAuth2AuthenticationSuccessHandler()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.accessDeniedHandler(jwtAccessDeniedHandler))
                .build();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtAuthenticationProvider,oAuth2Service,objectMapper);
    }


}
