package io.github.xxyopen.novel.resource.config;

import io.github.xxyopen.novel.common.constant.SystemConfigConsts;
import io.github.xxyopen.novel.resource.interceptor.FileInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final FileInterceptor fileInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(fileInterceptor)
                .addPathPatterns(SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY + "**")
                .order(1);
    }
}
