package io.github.xxyopen.novel.news.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.news.dao.entity.NewsContent;
import io.github.xxyopen.novel.news.dao.entity.NewsInfo;
import io.github.xxyopen.novel.news.dao.mapper.NewsContentMapper;
import io.github.xxyopen.novel.news.dao.mapper.NewsInfoMapper;
import io.github.xxyopen.novel.news.dto.resp.NewsInfoRespDto;
import io.github.xxyopen.novel.news.manager.cache.NewCacheManager;
import io.github.xxyopen.novel.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewCacheManager newCacheManager;

    private final NewsContentMapper newsContentMapper;
    private final NewsInfoMapper newsInfoMapper;

    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return newCacheManager.listLatestNews();
    }

    @Override
    public RestResp<NewsInfoRespDto> getNewsInfoById(Long newsId) {
        NewsInfo newsInfo = newsInfoMapper.selectById(newsId);
        NewsContent newsContent = newsContentMapper.selectOne(new LambdaQueryWrapper<NewsContent>().eq(NewsContent::getNewsId, newsId));
        return RestResp.ok(NewsInfoRespDto.builder()
                .title(newsInfo.getTitle())
                .content(newsContent.getContent()).sourceName(newsInfo.getSourceName())
                .categoryName(newsInfo.getCategoryName())
                .id(newsId)
                .categoryId(newsInfo.getCategoryId()).updateTime(newsInfo.getUpdateTime()).build());
    }
}
