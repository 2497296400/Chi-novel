package io.github.xxyopen.novel.book.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.xxyopen.novel.book.dao.entity.BookChapter;
import io.github.xxyopen.novel.book.dao.entity.BookComment;
import io.github.xxyopen.novel.book.dao.entity.BookContent;
import io.github.xxyopen.novel.book.dao.entity.BookInfo;
import io.github.xxyopen.novel.book.dao.mapper.*;
import io.github.xxyopen.novel.book.dto.req.*;
import io.github.xxyopen.novel.book.dto.resp.*;
import io.github.xxyopen.novel.book.manager.cache.*;
import io.github.xxyopen.novel.book.manager.feign.UserFeignManager;
import io.github.xxyopen.novel.book.manager.mq.AmqpMsgManager;
import io.github.xxyopen.novel.book.service.BookService;
import io.github.xxyopen.novel.common.constant.DatabaseConsts;
import io.github.xxyopen.novel.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.common.resp.PageRespDto;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.config.annotation.Key;
import io.github.xxyopen.novel.config.annotation.Lock;
import io.github.xxyopen.novel.config.exception.NovelEx;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookInfoMapper bookInfoMapper;
    private final BookCategoryMapper bookCategoryMapper;
    private final BookChapterMapper bookChapterMapper;

    private final BookCategoryCacheManager bookCategoryCacheManager;
    private final BookInfoCacheManager bookInfoCacheManager;
    private final bookChapterCacheManager bookChapterCacheManager;

    private final BookContentCacheManager bookContentCacheManager;

    private final Integer REC_BOOK_COUNT = 5;

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long bookId) {

        return RestResp.ok(bookInfoCacheManager.getBookInfo(bookId));
    }

    @Override
    public RestResp<Void> addVisitCount(Long bookId) {
        bookInfoMapper.addVisitCount(bookId);
        return RestResp.ok();

    }

    @Override
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId) {
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);
        if (bookInfo == null) {
            NovelEx.cast("小说信息不存在");
        }
        BookChapterRespDto bookChapterRespDto = bookChapterCacheManager.getChapter(bookInfo.getLastChapterId());
        if (bookChapterRespDto == null) {
            bookChapterCacheManager.exvict();
        }
        BookContent bookContent = bookContentCacheManager.getBookConnet(bookInfo.getLastChapterId());

        Long chapterTotal = bookChapterMapper.selectCount(new LambdaQueryWrapper<BookChapter>().eq(BookChapter::getBookId, bookId));

        return RestResp.ok(BookChapterAboutRespDto.builder().chapterTotal(chapterTotal).contentSummary(bookContent.getContent().substring(0, 30)).chapterInfo(bookChapterRespDto).build());
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) {
        Long categoryId = bookInfoCacheManager.getBookInfo(bookId).getCategoryId();
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);
        ArrayList<BookInfoRespDto> bookInfoRespDtoArrayList = new ArrayList<>(REC_BOOK_COUNT);
        Random random = new Random();
        int count = 0;
        while (count < REC_BOOK_COUNT) {
            int nextSize = random.nextInt(lastUpdateIdList.size());
            Long nextBookId = lastUpdateIdList.get(nextSize);
            BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(nextBookId);
            bookInfoRespDtoArrayList.add(bookInfo);
            count++;
        }
        return RestResp.ok(bookInfoRespDtoArrayList);
    }

    @Override
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        return RestResp.ok(bookChapterMapper.selectList(queryWrapper).stream().map(v -> BookChapterRespDto.builder().
                bookId(v.getBookId()).
                chapterName(v.getChapterName())
                .chapterNum(v.getChapterNum())
                .id(v.getId())
                .isVip(v.getIsVip())
                .chapterWordCount(v.getWordCount())
                .chapterUpdateTime(v.getUpdateTime())
                .build()
        ).toList());
    }

    @Override
    public RestResp<BookContentAboutRespDto> getConnent(Long chapterId) {
        BookContent bookConnet = bookContentCacheManager.getBookConnet(chapterId);
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());
        return RestResp.ok(BookContentAboutRespDto.builder().bookContent(bookConnet.getContent()).bookInfo(bookInfo).chapteInfo(chapter).build());
    }

    @Override
    public RestResp<Long> getPreChaptherId(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Integer nextChapterNum = Math.max(0, chapter.getChapterNum() - 1);
        Long bookId = chapter.getBookId();
        BookChapter bookChapter = bookChapterMapper.selectOne(new LambdaQueryWrapper<BookChapter>().eq(BookChapter::getBookId, bookId).eq(BookChapter::getChapterNum, nextChapterNum));

        return RestResp.ok(bookChapter.getId());
    }

    @Override
    public RestResp<Long> getNextChaptherId(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        BookChapter bookChapter = bookChapterMapper.selectOne(new LambdaQueryWrapper<BookChapter>().eq(BookChapter::getBookId, bookId).gt(BookChapter::getChapterNum, chapter.getChapterNum()).orderByAsc(BookChapter::getChapterNum).last(DatabaseConsts.SqlEnum.LIMIT_1.getSql()));
        return RestResp.ok(bookChapter == null ? chapterId : bookChapter.getId());
    }

    private final BookRankCacheManager bookRankCacheManager;

    @Override
    public RestResp<List<BookInfoRespDto>> listVisitRankBooks() {
        List<Long> bookIds = bookRankCacheManager.listVisitRankBookIds();
        return RestResp.ok(bookIds.stream().map(v -> bookInfoCacheManager.getBookInfo(v)
        ).toList());
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listNewestRankBooks() {
        List<Long> bookIds = bookRankCacheManager.listNewestRankBooks();
        return RestResp.ok(bookIds.stream().map(v -> bookInfoCacheManager.getBookInfo(v)
        ).toList());
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listUpdateRankBooks() {
        List<Long> bookIds = bookRankCacheManager.listUpdateRankBooks();
        return RestResp.ok(bookIds.stream().map(v -> bookInfoCacheManager.getBookInfo(v)
        ).toList());
    }

    private final BookCommentMapper bookCommentMapper;
    private final UserFeignManager userFeignManager;

    @Override
    public RestResp<BookCommentRespDto> listNewestComment(Long bookId) {
        Long commentCount = bookCommentMapper.selectCount(new LambdaQueryWrapper<BookComment>().eq(BookComment::getBookId, bookId));
        BookCommentRespDto bookCommentRespDtoBuilder = BookCommentRespDto.builder()
                .commentTotal(commentCount).build();
        if (commentCount > 0) {
            List<BookComment> comment = bookCommentMapper.selectList(new LambdaQueryWrapper<BookComment>().eq(BookComment::getBookId, bookId).orderByDesc(BookComment::getCreateTime).last(DatabaseConsts.SqlEnum.LIMIT_5.getSql()));
            List<Long> userId = comment.stream().map(v -> v.getUserId()).toList();
            List<UserInfoRespDto> listUserInfo = userFeignManager.listUserInfo(userId);
            Map<Long, UserInfoRespDto> userMapInfo =
                    listUserInfo.stream().collect(Collectors.toMap(UserInfoRespDto::getId, Function.identity()));
            List<BookCommentRespDto.CommentInfo> commentInfos = comment.stream().map(v -> {
                return BookCommentRespDto.CommentInfo.builder().id(v.getId())
                        .commentUserId(v.getUserId())
                        .commentTime(v.getCreateTime())
                        .commentUser(userMapInfo.get(v.getUserId()).getUsername())
                        .commentContent(v.getCommentContent())
                        .commentUserPhoto(userMapInfo.get(v.getUserId()).getUserPhoto())
                        .build();
            }).toList();
            bookCommentRespDtoBuilder.setComments(commentInfos);
        }
        return RestResp.ok(bookCommentRespDtoBuilder);
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listBookInfoByIds(List<Long> bookIds) {
        List<BookInfoRespDto> bookInfoRespDtos = bookIds.stream().map(v -> bookInfoCacheManager.getBookInfo(v)).toList();
        return RestResp.ok(bookInfoRespDtos);
    }

    @Override
    @Lock(prefix = "userComment")
    public RestResp<Void> publishComment(@Key(expr = "#{userId + '::' + bookId}") BookCommentReqDto bookCommentReqDto) {

        Long selectCount = bookCommentMapper.selectCount(new LambdaQueryWrapper<BookComment>().eq(BookComment::getBookId, bookCommentReqDto.getBookId()).eq(BookComment::getUserId, bookCommentReqDto.getUserId()));
        if (selectCount > 0) {
            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
        }

        BookComment bookComment = new BookComment();
        BeanUtils.copyProperties(bookCommentReqDto, bookComment);
        bookComment.setCreateTime(LocalDateTime.now());
        bookComment.setUpdateTime(LocalDateTime.now());
        bookCommentMapper.insert(bookComment);

        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateComment(BookCommentReqDto bookCommentReqDto) {
        BookComment bookComment = bookCommentMapper.selectOne(new LambdaQueryWrapper<BookComment>().eq(BookComment::getId, bookCommentReqDto.getCommentId()).eq(BookComment::getUserId, bookCommentReqDto.getUserId()));
        BeanUtils.copyProperties(bookCommentReqDto, bookComment);
        bookComment.setUpdateTime(LocalDateTime.now());
        bookCommentMapper.updateById(bookComment);
        return RestResp.ok();
    }


    @Override
    public RestResp<Void> deleteComment(BookCommentReqDto bookCommentReqDto) {
        BookComment bookComment = bookCommentMapper.selectById(bookCommentReqDto.getCommentId());
        bookCommentMapper.deleteById(bookCommentReqDto.getCommentId());
        Long bookId = bookComment.getBookId();
        BookInfo bookInfo = bookInfoMapper.selectById(bookId);
        bookInfo.setCommentCount(bookInfo.getCommentCount() - 1);
        bookInfoMapper.updateById(bookInfo);
        return RestResp.ok();
    }

    @Override
    public RestResp<BookChapterRespDto> getChapterById(Long chapterId) {
        return RestResp.ok(bookChapterCacheManager.getChapter(chapterId));
    }

    @Override
    public RestResp<Void> publishBook(BookAddReqDto bookAddReqDto) {
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookAddReqDto, bookInfo);
        if (bookInfoMapper.selectCount(new LambdaQueryWrapper<BookInfo>().eq(BookInfo::getBookName, bookAddReqDto.getBookName())) > 0) {
            NovelEx.cast("小说名字已经存在");
        }
        bookInfo.setCreateTime(LocalDateTime.now());
        bookInfo.setUpdateTime(LocalDateTime.now());
        bookInfo.setAuthorName(bookAddReqDto.getPenName());
        bookInfo.setScore(0);
        bookInfoMapper.insert(bookInfo);
        return null;
    }

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> listPublishBooks(BookPageReqDto dto) {
        Long authorId = dto.getAuthorId();
        IPage<BookInfo> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        IPage<BookInfo> bookInfoIPage = bookInfoMapper.selectPage(page, new LambdaQueryWrapper<BookInfo>().eq(BookInfo::getAuthorId, authorId).orderByDesc(BookInfo::getCreateTime));
        List<BookInfo> records = bookInfoIPage.getRecords();
        RestResp<PageRespDto<BookInfoRespDto>> resp = RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), bookInfoIPage.getTotal(),
                records.stream().map(v -> BookInfoRespDto.builder()
                        .bookStatus(v.getBookStatus())
                        .updateTime(v.getUpdateTime())
                        .bookName(v.getBookName())
                        .categoryName(v.getCategoryName())
                        .id(v.getId())
                        .bookDesc(v.getBookDesc())
                        .lastChapterName(v.getLastChapterName())
                        .authorName(v.getAuthorName())
                        .picUrl(v.getPicUrl())
                        .authorId(v.getAuthorId())
                        .wordCount(v.getWordCount()).build()
                ).toList()));
        return resp;
    }

    private final BookContentMapper bookContentMapper;

    private final AmqpMsgManager amqpMsgManager;

    /*
     *发布小说章节
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> publishBookChapter(ChapterAddReqDto dto) {
        Long authorId = dto.getAuthorId();
        Long bookId = dto.getBookId();

        BookInfo bookInfo = bookInfoMapper.selectById(bookId);
        if (!Objects.equals(bookInfo.getAuthorId(), authorId)) {
            NovelEx.cast("只能发布自己小说的章节");
        }
        bookInfo.setLastChapterName(dto.getChapterName());
        /*
         * 保存小说书籍信息
         * */

        BookChapter bookChapter = new BookChapter();
        bookChapter.setBookId(dto.getBookId());
        bookChapter.setChapterName(dto.getChapterName());
        bookChapter.setCreateTime(LocalDateTime.now());
        bookChapter.setUpdateTime(LocalDateTime.now());
        bookChapter.setWordCount(dto.getChapterContent().length());
        Long lastChapterId = bookInfo.getLastChapterId();
        int newLastChapterId = 1;
        if (lastChapterId != null) {
            BookChapterRespDto preChapter = bookChapterCacheManager.getChapter(bookInfo.getLastChapterId());
            newLastChapterId = preChapter.getChapterNum() + 1;
        }
        bookChapter.setChapterNum(newLastChapterId);
        bookChapter.setIsVip(dto.getIsVip());
        bookChapterMapper.insert(bookChapter);


        bookInfo.setLastChapterId(bookChapter.getId());
        bookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        bookInfo.setWordCount(bookInfo.getWordCount() + bookChapter.getWordCount());
        bookInfoMapper.updateById(bookInfo);

        BookContent bookContent = new BookContent();
        bookContent.setChapterId(bookChapter.getId());
        bookContent.setContent(dto.getChapterContent());
        bookContent.setCreateTime(LocalDateTime.now());
        bookContent.setUpdateTime(LocalDateTime.now());
        bookContentMapper.insert(bookContent);
        //清除小说缓存
        bookInfoCacheManager.evictBookInfoCache(bookId);
        //发送消息到mq
        amqpMsgManager.sendBookChangeMsg(bookId);
        return RestResp.ok();
    }

    @Override
    public RestResp<PageRespDto<BookChapterRespDto>> listPublishBookChapters(ChapterPageReqDto chapterPageReqDto) {
        IPage<BookChapter> page = new Page<>();
        page.setCurrent(chapterPageReqDto.getPageNum());
        page.setSize(chapterPageReqDto.getPageSize());
        IPage<BookChapter> bookChapterIPage = bookChapterMapper.selectPage(page, new LambdaQueryWrapper<BookChapter>().eq(BookChapter::getBookId, chapterPageReqDto.getBookId()).orderByDesc(BookChapter::getChapterNum));
        return RestResp.ok(PageRespDto.of(bookChapterIPage.getCurrent(), bookChapterIPage.getSize(), bookChapterIPage.getTotal(), bookChapterIPage.getRecords().stream().map(v -> BookChapterRespDto.builder()
                .chapterNum(v.getChapterNum())
                .bookId(v.getBookId())
                .chapterName(v.getChapterName())
                .chapterUpdateTime(v.getUpdateTime())
                .isVip(v.getIsVip())
                .id(v.getId())
                .chapterWordCount(v.getWordCount())
                .build()).toList()));
    }
}

