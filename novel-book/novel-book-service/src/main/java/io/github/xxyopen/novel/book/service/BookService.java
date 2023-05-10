package io.github.xxyopen.novel.book.service;

import io.github.xxyopen.novel.book.dto.req.*;
import io.github.xxyopen.novel.book.dto.resp.*;
import io.github.xxyopen.novel.common.resp.PageRespDto;
import io.github.xxyopen.novel.common.resp.RestResp;

import java.util.List;

public interface BookService 
{

    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);

    RestResp<BookInfoRespDto> getBookById(Long bookId);

    RestResp<Void> addVisitCount(Long bookId);

    RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId);

    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId);

    RestResp<List<BookChapterRespDto>> listChapters(Long bookId);

    RestResp<BookContentAboutRespDto> getConnent(Long chapterId);

    RestResp<Long> getPreChaptherId(Long chapterId);

    RestResp<Long> getNextChaptherId(Long chapterId);

    RestResp<List<BookInfoRespDto>> listVisitRankBooks();

    RestResp<List<BookInfoRespDto>> listNewestRankBooks();

    RestResp<List<BookInfoRespDto>> listUpdateRankBooks();

    RestResp<BookCommentRespDto> listNewestComment(Long bookId);

    RestResp<List<BookInfoRespDto>> listBookInfoByIds(List<Long> bookIds);

    RestResp<Void> publishComment(BookCommentReqDto bookCommentReqDto);

    RestResp<Void> updateComment(BookCommentReqDto bookCommentReqDto);

    RestResp<Void> deleteComment(BookCommentReqDto bookCommentReqDto);

    RestResp<BookChapterRespDto> getChapterById(Long chapterId);
    

    RestResp<Void> publishBook(BookAddReqDto bookAddReqDto);

    RestResp<PageRespDto<BookInfoRespDto>> listPublishBooks(BookPageReqDto dto);

    RestResp<Void> publishBookChapter(ChapterAddReqDto dto);

    RestResp<PageRespDto<BookChapterRespDto>> listPublishBookChapters(ChapterPageReqDto chapterPageReqDto);
}
