package backend.dev.setting.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String tokenByCookie = getTokenByCookie(httpServletRequest);
        String tokenByHeader = getTokenByHeader(httpServletRequest);
        if (StringUtils.hasText(tokenByHeader) &&) {
            tokenByHeader
        }
        
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String getTokenByHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken.startsWith("Bearer") && bearerToken.length() > 7) {
            return bearerToken.substring(7);
        }
    }

    private String getTokenByCookie(HttpServletRequest httpServletRequest) {

        if()
    }
}
