package io.github.xxyopen.novel.book.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookCommentReqDto {

    private Long commentId;

    private Long userId;
    @Schema(description = "小说ID")
    private Long bookId;

    @Schema(description = "评论内容")
    private String commentContent;

}
