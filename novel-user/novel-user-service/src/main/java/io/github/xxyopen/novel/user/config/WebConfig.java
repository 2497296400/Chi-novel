package io.github.xxyopen.novel.user.config;

import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/**")
                .excludePathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/register", ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/login")
                .order(2);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
