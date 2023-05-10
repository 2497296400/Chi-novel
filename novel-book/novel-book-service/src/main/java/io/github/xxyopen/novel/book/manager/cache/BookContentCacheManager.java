package io.github.xxyopen.novel.book.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.book.dao.entity.BookContent;
import io.github.xxyopen.novel.book.dao.mapper.BookContentMapper;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookContentCacheManager {
    private final BookContentMapper bookContentMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_CONTENT_CACHE_NAME)
    public BookContent getBookConnet(Long chapterId) {
        
        return bookContentMapper.selectOne(
                new LambdaQueryWrapper<BookContent>().
                        eq(BookContent::getChapterId, chapterId));
    }
}
