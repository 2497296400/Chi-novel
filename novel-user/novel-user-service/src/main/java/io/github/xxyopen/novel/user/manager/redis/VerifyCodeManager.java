package io.github.xxyopen.novel.user.manager.redis;

import io.github.xxyopen.novel.common.constant.CacheConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {
    private final StringRedisTemplate redisTemplate;

    public boolean imgVerifyCodeOk(String sessionId, String verifyCode) {
        return Objects.equals(redisTemplate.opsForValue().get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId), verifyCode);
    }
    
    public void removeImgVerifyCode(String sessionId) {
        redisTemplate.delete(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId);
    }
    
}
