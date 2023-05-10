package io.github.xxyopen.novel.book.controller.front;

import io.github.xxyopen.novel.book.dto.resp.*;
import io.github.xxyopen.novel.book.service.BookService;
import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FrontBookController", description = "前台门户-小说模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
@RequiredArgsConstructor

public class FrontBookController {
    private final BookService bookService;
    /**
     * 小说分类列表查询接口
     * .
     */

    @Operation(summary = "小说列表查询接口")
    @GetMapping("categort/list")
    public RestResp<List<BookCategoryRespDto>> listCategory(
            @Parameter(description = "作品方向", required = true) @RequestParam Integer workDirection) {

        return bookService.listCategory(workDirection);
    }

    /**
     * 小说信息查询接口
     */
    @Operation(summary = "小说信息查询接口")
    @GetMapping("{id}")
    public RestResp<BookInfoRespDto> getBookById(@Parameter(description = "小说 Id") @PathVariable("id") Long bookId) {
        return bookService.getBookById(bookId);
    }

    /**
     * 增加小说点击量接口
     */
    @Operation(summary = "增加小说点击量")
    @PostMapping("/visit")
    private RestResp<Void> addVisitCount(@Parameter(description = "小说Id") Long bookId) {
        return bookService.addVisitCount(bookId);
    }

    /**
     * 小说最新章节相关信息查询接口
     */
    @Operation(summary = "小说最新章节相关信息查询接口")
    @GetMapping("last_chapter/about")
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(
            @Parameter(description = "小说ID") @RequestParam Long bookId) {
        return bookService.getLastChapterAbout(bookId);
    }

    @Operation(summary = "小说推荐列表查询")
    @GetMapping("rec_list")
    public RestResp<List<BookInfoRespDto>> listRecBooks(@Parameter(description = "小说ID") @RequestParam Long bookId) {
        return bookService.listRecBooks(bookId);
    }

    @Operation(summary = "小说章节列表查询")
    @GetMapping("chapter/list")
    public RestResp<List<BookChapterRespDto>> listChapters(@Parameter(description = "小说ID") @RequestParam Long bookId) {
        return bookService.listChapters(bookId);
    }

    @Operation(summary = "小说内容")
    @GetMapping("connent/{chapterId}")
    public RestResp<BookContentAboutRespDto> getBookConnet(@Parameter(description = "章节Id") @PathVariable(value = "chapterId") Long chapterId) {
        return bookService.getConnent(chapterId);
    }

    @Operation(summary = "获取上一章节Id")
    @GetMapping("pre_chapter_id/{chapterId}")
    public RestResp<Long> getPreChaptherId(@Parameter(description = "章节Id") @PathVariable(value = "chapterId") Long chapterId) {
        return bookService.getPreChaptherId(chapterId);
    }

    @Operation(summary = "获取下一章节Id")
    @GetMapping("next_chapter_id/{chapterId}")
    public RestResp<Long> getNextChaptherId(@Parameter(description = "章节Id") @PathVariable(value = "chapterId") Long chapterId) {
        return bookService.getNextChaptherId(chapterId);
    }

    @Operation(summary = "小说点击榜查询接口")
    @GetMapping("visit_rank")
    public RestResp<List<BookInfoRespDto>> listVisitRankBooks() {
        return bookService.listVisitRankBooks();
    }

    @Operation(summary = "小说新书榜")
    @GetMapping("newest_rank")
    public RestResp<List<BookInfoRespDto>> listNewestRankBooks() {
        return bookService.listNewestRankBooks();
    }


    /**
     * 小说更新榜查询接口
     */
    @Operation(summary = "小说更新榜单")
    @GetMapping("update_rank")
    public RestResp<List<BookInfoRespDto>> listUpdateRankBooks() {
        return bookService.listUpdateRankBooks();
    }

    @Operation(summary = "小说最新评论接口")
    @GetMapping("comment/newest_list")
    public RestResp<BookCommentRespDto> listNewestComment(@Parameter(description = "图书Id") @RequestParam Long bookId) {
        return bookService.listNewestComment(bookId);
    }
}
