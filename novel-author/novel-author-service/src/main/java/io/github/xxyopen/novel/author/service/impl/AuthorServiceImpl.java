package io.github.xxyopen.novel.author.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.author.dao.entity.AuthorInfo;
import io.github.xxyopen.novel.author.dao.mapper.AuthorInfoMapper;
import io.github.xxyopen.novel.author.dto.AuthorInfoDto;
import io.github.xxyopen.novel.author.dto.req.AuthorRegisterReqDto;
import io.github.xxyopen.novel.author.manager.cache.AuthorInfoCacheManager;
import io.github.xxyopen.novel.author.service.AuthorService;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.config.exception.NovelEx;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthorServiceImpl implements AuthorService {
    private final AuthorInfoMapper authorInfoMapper;
    private final AuthorInfoCacheManager authorInfoCacheManager;

    @Override
    public RestResp<Void> register(AuthorRegisterReqDto authorRegisterReqDto) {
        authorRegisterReqDto.setUserId(UserHolder.getUserId());
        AuthorInfo authorInfo = authorInfoMapper.selectOne(new LambdaQueryWrapper<AuthorInfo>().eq(AuthorInfo::getUserId, authorRegisterReqDto.getUserId()));
        if (authorInfo != null) {
            NovelEx.cast("该用户已成为作家");
        }
        authorInfo = new AuthorInfo();
        BeanUtils.copyProperties(authorRegisterReqDto, authorInfo);
        authorInfoMapper.insert(authorInfo);
        authorInfoCacheManager.evictAuthorCache();
        return RestResp.ok();
    }

    @Override
    public RestResp<Integer> getStatus(Long userId) {
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(userId);
        return author == null ? RestResp.ok(null) : RestResp.ok(author.getStatus());
    }
}
