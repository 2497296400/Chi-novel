package io.github.xxyopen.novel.news.service;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.news.dto.resp.NewsInfoRespDto;

import java.util.List;

public interface NewsService {
    RestResp<List<NewsInfoRespDto>> listLatestNews();

    RestResp<NewsInfoRespDto> getNewsInfoById(Long newsId);
}
