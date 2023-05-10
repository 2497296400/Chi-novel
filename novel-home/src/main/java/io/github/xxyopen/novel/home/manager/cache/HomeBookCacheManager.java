package io.github.xxyopen.novel.home.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.xxyopen.novel.common.constant.CacheConsts;
import io.github.xxyopen.novel.common.constant.DatabaseConsts;
import io.github.xxyopen.novel.home.dao.entity.HomeBook;
import io.github.xxyopen.novel.home.dao.mapper.HomeBookMapper;
import io.github.xxyopen.novel.home.dto.resp.HomeBookRespDto;
import io.github.xxyopen.novel.home.manager.feign.BookFeignManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页推荐小说 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class HomeBookCacheManager {
    private final HomeBookMapper homeBookMapper;
    private final BookFeignManager bookFeignManager;
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.HOME_BOOK_CACHE_NAME)
    public List<HomeBookRespDto> listHomeBooks() {
        QueryWrapper<HomeBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        List<HomeBook> homeBooks = homeBookMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(homeBooks)) {
            List<Long> bookIds = homeBooks.stream().map(HomeBook::getBookId).toList();
            List<BookInfoRespDto> bookInfoRespDtos = bookFeignManager.listBookInfoByIds(bookIds);
            if (!CollectionUtils.isEmpty(bookInfoRespDtos)) {
                Map<Long, BookInfoRespDto> respDtoMap = bookInfoRespDtos.stream().collect(Collectors.toMap(BookInfoRespDto::getId, Function.identity()));
                return homeBooks.stream().map(v -> {
                    BookInfoRespDto bookInfoRespDto = respDtoMap.get(v.getBookId());
                    HomeBookRespDto homeBookRespDto = new HomeBookRespDto();
                    homeBookRespDto.setBookName(bookInfoRespDto.getBookName());
                    homeBookRespDto.setBookId(v.getBookId());
                    homeBookRespDto.setType(v.getType());
                    homeBookRespDto.setPicUrl(homeBookRespDto.getPicUrl());
                    homeBookRespDto.setBookDesc(homeBookRespDto.getBookDesc());
                    homeBookRespDto.setAuthorName(homeBookRespDto.getAuthorName());
                    return homeBookRespDto;
                }).toList();
            }
        }
        return Collections.emptyList();
    }
    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.HOME_BOOK_CACHE_NAME)
    public void evictCache(){
        
    }
}
