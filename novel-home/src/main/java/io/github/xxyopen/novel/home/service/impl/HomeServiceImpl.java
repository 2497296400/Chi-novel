package io.github.xxyopen.novel.home.service.impl;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.home.dto.resp.HomeBookRespDto;
import io.github.xxyopen.novel.home.dto.resp.HomeFriendLinkRespDto;
import io.github.xxyopen.novel.home.manager.cache.FriendLinkCacheManager;
import io.github.xxyopen.novel.home.manager.cache.HomeBookCacheManager;
import io.github.xxyopen.novel.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 首页模块 服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/13
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final HomeBookCacheManager homeBookCacheManager;
    private final FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        List<HomeBookRespDto> homeBookRespDtos = homeBookCacheManager.listHomeBooks();
        if (CollectionUtils.isEmpty(homeBookRespDtos)) {
            homeBookCacheManager.evictCache();
        }
        return RestResp.ok(homeBookRespDtos);
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        List<HomeFriendLinkRespDto> friendLinkRespDtos = friendLinkCacheManager.listFriendLinks();
        if (CollectionUtils.isEmpty(friendLinkRespDtos)) {
            friendLinkCacheManager.cacheEvict();
        }
        return RestResp.ok(friendLinkRespDtos);
    }
}
