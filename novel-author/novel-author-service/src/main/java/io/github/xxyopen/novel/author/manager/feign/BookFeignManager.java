package io.github.xxyopen.novel.author.manager.feign;

import io.github.xxyopen.novel.author.manager.cache.AuthorInfoCacheManager;
import io.github.xxyopen.novel.book.dto.req.BookAddReqDto;
import io.github.xxyopen.novel.book.dto.req.BookPageReqDto;
import io.github.xxyopen.novel.book.dto.req.ChapterAddReqDto;
import io.github.xxyopen.novel.book.dto.req.ChapterPageReqDto;
import io.github.xxyopen.novel.book.dto.resp.BookChapterRespDto;
import io.github.xxyopen.novel.book.dto.resp.BookInfoRespDto;
import io.github.xxyopen.novel.book.feign.BookFeign;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.resp.PageRespDto;
import io.github.xxyopen.novel.common.resp.RestResp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookFeignManager {
    private final BookFeign bookFeign;

    private final AuthorInfoCacheManager authorInfoCacheManager;

    public RestResp<Void> publishBook(BookAddReqDto bookAddReqDto) {
        bookAddReqDto.setAuthorId(UserHolder.getAuthorId());
        return bookFeign.publishBook(bookAddReqDto);
    }


    public RestResp<PageRespDto<BookInfoRespDto>> listPublishBooks(BookPageReqDto dto) {
        authorInfoCacheManager.getAuthor(UserHolder.getUserId());
        RestResp<PageRespDto<BookInfoRespDto>> pageRespDtoRestResp = bookFeign.listPublishBooks(dto);

        return pageRespDtoRestResp;
    }

    public RestResp<Void> publishBookChapter(ChapterAddReqDto addReqDto) {
        return bookFeign.publishBookChapter(addReqDto);
    }


    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(ChapterPageReqDto chapterPageReqDto) {
        return  bookFeign.listPublishBookChapters(chapterPageReqDto);
    }
}
