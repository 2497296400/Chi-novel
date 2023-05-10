package io.github.xxyopen.novel.author.controller.author;


import io.github.xxyopen.novel.author.dto.req.AuthorRegisterReqDto;
import io.github.xxyopen.novel.author.manager.cache.AuthorInfoCacheManager;
import io.github.xxyopen.novel.author.manager.feign.BookFeignManager;
import io.github.xxyopen.novel.author.service.AuthorService;
import io.github.xxyopen.novel.book.dto.req.BookAddReqDto;
import io.github.xxyopen.novel.book.dto.req.BookPageReqDto;
import io.github.xxyopen.novel.book.dto.req.ChapterAddReqDto;
import io.github.xxyopen.novel.book.dto.req.ChapterPageReqDto;
import io.github.xxyopen.novel.book.dto.resp.BookChapterRespDto;
import io.github.xxyopen.novel.book.dto.resp.BookInfoRespDto;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.req.PageReqDto;
import io.github.xxyopen.novel.common.resp.PageRespDto;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apiguardian.api.API;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AuthorController", description = "作家端口")
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookFeignManager bookFeignManager;

    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto authorRegisterReqDto) {
        return authorService.register(authorRegisterReqDto);
    }

    @Operation(summary = "作家状态")
    @PostMapping("status")
    public RestResp<Integer> getStatus() {
        return authorService.getStatus(UserHolder.getUserId());
    }

    @Operation(summary = "小说发布接口")
    @PostMapping("book")
    public RestResp<Void> publishBook(@Valid @RequestBody BookAddReqDto bookAddReqDto) {
        RestResp<Void> restResp = bookFeignManager.publishBook(bookAddReqDto);

        return restResp;
    }

    @Operation(summary = "小说发布列表查询接口")
    @PostMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> listPublishBooks(@ParameterObject BookPageReqDto dto) {
        dto.setAuthorId(UserHolder.getAuthorId());
        RestResp<PageRespDto<BookInfoRespDto>> pageRespDtoRestResp = bookFeignManager.listPublishBooks(dto);
        return pageRespDtoRestResp;
    }

    @Operation(summary = "小说章节发布")
    @PostMapping("book/chapter/{bookId}")
    public RestResp<Void> publishBookChapter(@Parameter(description = "小说ID") @PathVariable("bookId") Long bookId,
                                             @Valid @RequestBody ChapterAddReqDto addReqDto) {
        addReqDto.setAuthorId(UserHolder.getAuthorId());
        addReqDto.setBookId(bookId);
        return bookFeignManager.publishBookChapter(addReqDto);
    }

    @Operation(summary = "章节列表查询")
    @PostMapping("list/book/chapters/{bookId}")
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(@Parameter(description = "小说Id") @PathVariable("bookId") Long bookId, @ParameterObject ChapterPageReqDto chapterPageReqDto) {
        chapterPageReqDto.setBookId(bookId);
        return bookFeignManager.listBookChapters(chapterPageReqDto);
    }
}
