package io.github.xxyopen.novel.book.dto.req;

import io.github.xxyopen.novel.common.req.PageReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChapterPageReqDto  extends PageReqDto {
    /**
     * 小说ID
     */
    @NotBlank
    @Schema(description = "小说ID", required = true)
    private Long bookId;
    
}
