package io.github.xxyopen.novel.resource.manager.redis;

import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.resource.util.ImgVerifyCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {
    private final StringRedisTemplate redisTemplate;

    public String genImgVerifyCode(String sessionId) throws IOException {
        String verifyCode = ImgVerifyCodeUtils.getRandomVerifyCode(4);
        String img = ImgVerifyCodeUtils.genVerifyCodeImg(verifyCode);
        redisTemplate.opsForValue().set(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId,
                verifyCode, Duration.ofMinutes(5));
        return img;
    }
}
