package io.github.xxyopen.novel.book.controller.inner;

import io.github.xxyopen.novel.book.dto.req.*;
import io.github.xxyopen.novel.book.dto.resp.BookChapterRespDto;
import io.github.xxyopen.novel.book.dto.resp.BookInfoRespDto;
import io.github.xxyopen.novel.book.service.BookService;
import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.resp.PageRespDto;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "InnerBookController", description = "内部调用-接口")
@RestController
@RequestMapping(ApiRouterConsts.API_INNER_BOOK_URL_PREFIX)
@RequiredArgsConstructor
public class InnerBookController {
    private final BookService bookService;

    @Operation(description = "查询小说信息")
    @PostMapping("listBookInfoByIds")
    public RestResp<List<BookInfoRespDto>> listBookInfoByIds(@Parameter(description = "书籍Id") @RequestBody List<Long> bookIds) {
        return bookService.listBookInfoByIds(bookIds);
    }

    @Operation(description = "发布评论接口")
    @PostMapping("publishComment")
    public RestResp<Void> publishComment(@RequestBody BookCommentReqDto bookCommentReqDto) {
        return bookService.publishComment(bookCommentReqDto);
    }

    @Operation(description = "修改评论接口")
    @PostMapping("updateComment")
    public RestResp<Void> updateComment(@RequestBody BookCommentReqDto bookCommentReqDto) {
        return bookService.updateComment(bookCommentReqDto);
    }

    @Operation(description = "删除评论接口")
    @PostMapping("deleteComment")
    public RestResp<Void> deleteComment(@RequestBody BookCommentReqDto bookCommentReqDto) {
        return bookService.deleteComment(bookCommentReqDto);
    }

    @Operation(description = "小说章节查询接口")
    @PostMapping("getChapterById")
    public RestResp<BookChapterRespDto> getChapterById(@Parameter(description = "章节Id") @RequestBody Long chapterId) {
        return bookService.getChapterById(chapterId);
    }

    @Operation(description = "查询小说信息")
    @PostMapping("getBookInfoById")
    public RestResp<BookInfoRespDto> getBookInfoById(@Parameter(description = "小说ID") @RequestBody Long bookId) {
        return bookService.getBookById(bookId);
    }

    @Operation(description = "小说发布接口")
    @PostMapping("publishBook")
    public RestResp<Void> publishBook(@RequestBody BookAddReqDto bookAddReqDto) {
        return bookService.publishBook(bookAddReqDto);
    }

    @Operation(description = "小说发布列表查询接口")
    @PostMapping("listPublishBooks")
    public RestResp<PageRespDto<BookInfoRespDto>> listPublishBooks(@RequestBody BookPageReqDto dto) {
        RestResp<PageRespDto<BookInfoRespDto>> pageRespDtoRestResp = bookService.listPublishBooks(dto);
        return pageRespDtoRestResp;
    }

    @Operation(description = "章节发布接口")
    @PostMapping("publishBookChapter")
    public RestResp<Void> publishBookChapter(@RequestBody ChapterAddReqDto dto) {
        return bookService.publishBookChapter(dto);
    }

    @Operation(description = "小说章节查询接口")
    @PostMapping("listPublishBookChapters")
    public RestResp<PageRespDto<BookChapterRespDto>> listPublishBookChapters( ChapterPageReqDto chapterPageReqDto) {
        return  bookService.listPublishBookChapters(chapterPageReqDto);
    }

}
