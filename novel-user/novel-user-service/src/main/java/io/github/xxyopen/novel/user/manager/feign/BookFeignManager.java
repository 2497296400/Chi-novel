package io.github.xxyopen.novel.user.manager.feign;

import io.github.xxyopen.novel.book.dto.req.BookCommentReqDto;
import io.github.xxyopen.novel.book.feign.BookFeign;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.resp.RestResp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookFeignManager {
    private final   BookFeign bookFeign;

    public RestResp<Void> publishComment(BookCommentReqDto bookCommentReqDto) {
        bookCommentReqDto.setUserId(UserHolder.getUserId());
        return bookFeign.publishComment(bookCommentReqDto);
    }

    public RestResp<Void> updateComment(BookCommentReqDto bookCommentReqDto) {
        bookCommentReqDto.setUserId(UserHolder.getUserId());
        return bookFeign.updateComment(bookCommentReqDto);

    }

    public RestResp<Void> deleteComment(BookCommentReqDto bookCommentReqDto) {
        bookCommentReqDto.setUserId(UserHolder.getUserId());
        return  bookFeign.deleteComment(bookCommentReqDto);
    }
}
