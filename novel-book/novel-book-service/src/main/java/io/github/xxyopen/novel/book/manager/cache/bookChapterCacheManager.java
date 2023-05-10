package io.github.xxyopen.novel.book.manager.cache;

import io.github.xxyopen.novel.book.dao.entity.BookChapter;
import io.github.xxyopen.novel.book.dao.mapper.BookChapterMapper;
import io.github.xxyopen.novel.book.dto.resp.BookChapterRespDto;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class bookChapterCacheManager {
    private final BookChapterMapper bookChapterMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterRespDto getChapter(Long lastId) {
        BookChapter chapter = bookChapterMapper.selectById(lastId);
        return BookChapterRespDto.builder().bookId(chapter.getBookId()).
                chapterName(chapter.getChapterName())
                .chapterNum(chapter.getChapterNum())
                .chapterUpdateTime(chapter.getUpdateTime())
                 .chapterWordCount(chapter.getWordCount())
                .isVip(chapter.getIsVip())
                .bookId(chapter.getBookId())
                .build();
    }

    @CacheEvict(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public void exvict() {
        
    }
}
