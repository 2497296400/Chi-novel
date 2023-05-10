package io.github.xxyopen.novel.config.interceptor;

import io.github.xxyopen.novel.common.auth.JwtUtils;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.constant.SystemConfigConsts;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class TokenParseInterceptor implements HandlerInterceptor {
    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(SystemConfigConsts.HTTP_AUTH_HEADER_NAME);
        if (StringUtils.hasText(token)) {
            UserHolder.setUserId(JwtUtils.parseToken(token, SystemConfigConsts.NOVEL_FRONT_KEY));
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * DispatcherServlet 完全处理完请求后调用，出现异常照常调用
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
