package io.github.xxyopen.novel.user.controller.front;

import io.github.xxyopen.novel.book.dto.req.BookCommentReqDto;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.constant.SystemConfigConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.user.dto.req.UserInfoUptReqDto;
import io.github.xxyopen.novel.user.dto.req.UserLoginReqDto;
import io.github.xxyopen.novel.user.dto.req.UserRegisterReqDto;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserLoginRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserRegisterRespDto;
import io.github.xxyopen.novel.user.manager.feign.BookFeignManager;
import io.github.xxyopen.novel.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@Tag(name = "UserController", description = "前台门户-会员模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
public class FrontUserController {
    private final UserService userService;

    private final BookFeignManager bookFeignManager;

    @Operation(summary = "用户注册接口")
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto) {
        return userService.login(userLoginReqDto);
    }

    @Operation(summary = "用户信息查询接口")
    @GetMapping("get/userinfo")
    public RestResp<UserInfoRespDto> getUserInfo() {
        return userService.getUserInfo(UserHolder.getUserId());
    }

    @Operation(summary = "用户信息修改")
    @PutMapping("update/userinfo")
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto userInfoUptReqDto) {
        return userService.updateUserInfo(userInfoUptReqDto);
    }

    @Operation(summary = "用户反馈")
    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String conent) {
        return userService.submitFeedback(UserHolder.getUserId(), conent);
    }

    @Operation(summary = "用户反馈删除接口")
    @GetMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@Parameter(description = "反馈Id") @PathVariable("id") Long Id) {
        return userService.deleteFeedback(Id);
    }

    @Operation(summary = "发布评论")
    @PostMapping("comment")
    public RestResp<Void> publistComment(@RequestBody BookCommentReqDto bookCommentReqDto) {

        return bookFeignManager.publishComment(bookCommentReqDto);
    }

    @Operation(summary = "修改评论")
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@Parameter(description = "评论Id") @PathVariable Long id, String content) {
        BookCommentReqDto bookCommentReqDto = new BookCommentReqDto();
        bookCommentReqDto.setCommentId(id);
        bookCommentReqDto.setCommentContent(content);
        return bookFeignManager.updateComment(bookCommentReqDto);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@Parameter(description = "评论Id") @PathVariable Long id) {
        BookCommentReqDto bookCommentReqDto = new BookCommentReqDto();
        bookCommentReqDto.setCommentId(id);
        return bookFeignManager.deleteComment(bookCommentReqDto);
    }

    @Operation(summary = "查询书架状态")
    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(@Parameter(description = "小说ID") Long bookId) {
        return userService.getBookshelfStatus(UserHolder.getUserId(), bookId);
    }

    @Operation(summary = "添加书架接口")
    @PostMapping("bookshelf")
    public RestResp<Void> addBookshelf(@Parameter(description = "章节Id") Long chapterId) {
        return userService.addBookshelf(chapterId);
    }
}
