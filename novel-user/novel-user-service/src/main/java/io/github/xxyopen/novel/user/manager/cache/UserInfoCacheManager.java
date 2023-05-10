package io.github.xxyopen.novel.user.manager.cache;

import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.user.dao.entity.UserInfo;
import io.github.xxyopen.novel.user.dao.mapper.UserInfoMapper;
import io.github.xxyopen.novel.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {

    private final UserInfoMapper userInfoMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDto getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return  UserInfoDto.builder().id(userInfo.getId()).status(userInfo.getStatus()).build();
    }
}
