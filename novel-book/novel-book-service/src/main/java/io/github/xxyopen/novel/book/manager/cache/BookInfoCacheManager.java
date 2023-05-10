package io.github.xxyopen.novel.book.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.xxyopen.novel.book.dao.entity.BookCategory;
import io.github.xxyopen.novel.book.dao.entity.BookChapter;
import io.github.xxyopen.novel.book.dao.entity.BookInfo;
import io.github.xxyopen.novel.book.dao.mapper.BookCategoryMapper;
import io.github.xxyopen.novel.book.dao.mapper.BookChapterMapper;
import io.github.xxyopen.novel.book.dao.mapper.BookInfoMapper;
import io.github.xxyopen.novel.book.dto.resp.BookInfoRespDto;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.common.constant.DatabaseConsts;
import io.github.xxyopen.novel.config.exception.NovelEx;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookInfoCacheManager {
    private final BookInfoMapper bookInfoMapper;

    private final BookChapterMapper chapterMapper;

    /**
     * 从缓存中查询小说信息（先判断缓存中是否已存在，存在则直接从缓存中取，否则执行方法体中的逻辑后缓存结果）
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long id) {
        return cachePutBookInfo(id);
    }

    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto cachePutBookInfo(Long id) {
        BookInfo bookInfo = bookInfoMapper.selectById(id);
        if (bookInfo == null) {
            NovelEx.cast("图书数据不能为空");
        }
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, id)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter firstBookChapter = chapterMapper.selectOne(queryWrapper);
        return BookInfoRespDto.builder()
                .bookDesc(bookInfo.getBookDesc())
                .firstChapterId(firstBookChapter.getId())
                .wordCount(bookInfo.getWordCount())
                .bookName(bookInfo.getBookName())
                .picUrl(bookInfo.getPicUrl())
                .bookStatus(bookInfo.getBookStatus())
                .visitCount(bookInfo.getVisitCount())
                .authorId(bookInfo.getAuthorId())
                .lastChapterId(bookInfo.getLastChapterId())
                .commentCount(bookInfo.getCommentCount())
                .id(bookInfo.getId())
                .categoryId(bookInfo.getCategoryId())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .lastChapterName(bookInfo.getLastChapterName())
                .categoryName(bookInfo.getCategoryName())
                .updateTime(bookInfo.getUpdateTime())
                .build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfoCache(Long ignoredId) {
        // 调用此方法自动清除小说信息的缓存
    }

    /**
     * 查询每个类别下最新更新的 500 个小说ID列表，并放入缓存中 1 个小时
     */
    /**
     * 查询每个类别下最新更新的 500 个小说ID列表，并放入缓存中 1 个小时
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.LAST_UPDATE_BOOK_ID_LIST_CACHE_NAME)
    public List<Long> getLastUpdateIdList(Long categoryId) {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.COLUMN_CATEGORY_ID, categoryId)
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                .orderByDesc(DatabaseConsts.BookTable.COLUMN_LAST_CHAPTER_UPDATE_TIME)
                .last(DatabaseConsts.SqlEnum.LIMIT_500.getSql());
        return bookInfoMapper.selectList(queryWrapper).stream()
                .map(BookInfo::getId).toList();
    }
}
