package io.github.xxyopen.novel.book.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.book.dao.entity.BookInfo;
import io.github.xxyopen.novel.book.dao.mapper.BookInfoMapper;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.common.constant.DatabaseConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookRankCacheManager {
    private final BookInfoMapper bookInfoMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_VISIT_RANK_CACHE_NAME)
    public List<Long> listVisitRankBookIds() {
        return bookInfoMapper.selectList(new LambdaQueryWrapper<BookInfo>().
                        orderByDesc(BookInfo::getVisitCount).
                        last(DatabaseConsts.SqlEnum.LIMIT_30.getSql()))
                .stream().
                map(BookInfo::getId).toList();
    }

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_NEWEST_RANK_CACHE_NAME)
    public List<Long> listNewestRankBooks() {
        return bookInfoMapper.selectList(new LambdaQueryWrapper<BookInfo>().orderByDesc(BookInfo::getCreateTime).orderByDesc(BookInfo::getVisitCount).last(DatabaseConsts.SqlEnum.LIMIT_30.getSql()))
                .stream().map(BookInfo::getId).toList();
    }
    
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,value = CacheConsts.BOOK_UPDATE_RANK_CACHE_NAME)
    public List<Long> listUpdateRankBooks() {
        return bookInfoMapper.selectList(new LambdaQueryWrapper<BookInfo>().orderByDesc(BookInfo::getUpdateTime).orderByDesc(BookInfo::getVisitCount).last(DatabaseConsts.SqlEnum.LIMIT_30.getSql()))
                .stream().map(BookInfo::getId).toList();
    }
}
