package io.github.xxyopen.novel.user.config;

import io.github.xxyopen.novel.common.auth.JwtUtils;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.common.constant.SystemConfigConsts;
import io.github.xxyopen.novel.config.exception.BusinessException;
import io.github.xxyopen.novel.config.exception.NovelEx;
import io.github.xxyopen.novel.user.dto.UserInfoDto;
import io.github.xxyopen.novel.user.manager.cache.UserInfoCacheManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    /*    String token = request.getHeader(SystemConfigConsts.HTTP_AUTH_HEADER_NAME);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        Long userId = JwtUtils.parseToken(token, SystemConfigConsts.NOVEL_AUTHOR_KEY);
        if (Objects.isNull(userId)) {
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        UserInfoDto userInfo = userInfoCacheManager.getUser(userId);
        if (Objects.isNull(userInfo)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }*/
        UserHolder.setUserId(1L);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
