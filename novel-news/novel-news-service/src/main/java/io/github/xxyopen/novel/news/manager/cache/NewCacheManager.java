package io.github.xxyopen.novel.news.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.common.constant.DatabaseConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.news.dao.entity.NewsInfo;
import io.github.xxyopen.novel.news.dao.mapper.NewsInfoMapper;
import io.github.xxyopen.novel.news.dto.resp.NewsInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewCacheManager {
    private final NewsInfoMapper newsInfoMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return RestResp.ok(newsInfoMapper.selectList(new LambdaQueryWrapper<NewsInfo>().orderByDesc(NewsInfo::getCreateTime).last(DatabaseConsts.SqlEnum.LIMIT_30.getSql()))
                .stream().map(v -> NewsInfoRespDto.builder()
                        .title(v.getTitle())
                        .sourceName(v.getSourceName())
                        .updateTime(v.getUpdateTime())
                        .categoryId(v.getCategoryId())
                        .id(v.getId())
                        .build()).toList());
    }
    
}
