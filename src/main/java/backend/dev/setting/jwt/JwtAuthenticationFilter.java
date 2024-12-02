package backend.dev.setting.jwt;

import backend.dev.user.DTO.users.UserDTO;
import backend.dev.user.entity.Role;
import backend.dev.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final Jwt jwt;
    private final UserService userService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String tokenByHeader = getAccessTokenByHeader(httpServletRequest);
        if (StringUtils.hasText(tokenByHeader)&&jwt.verify(tokenByHeader)&& jwt.isAccessToken(tokenByHeader)) {
            Claims claims = jwt.parseClaims(tokenByHeader);
            String userId = claims.getSubject();
            List<GrantedAuthority> authorities = getAuthorities(userService.findMyInformation(userId));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private List<GrantedAuthority> getAuthorities(UserDTO myInformation) {
        Role role = myInformation.role();
        return role == null ? Collections.emptyList() : List.of(new SimpleGrantedAuthority("ROLE_"+ role));
    }

    private String getAccessTokenByHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer") && bearerToken.length() > 7) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getAccessTokenByCookie(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
