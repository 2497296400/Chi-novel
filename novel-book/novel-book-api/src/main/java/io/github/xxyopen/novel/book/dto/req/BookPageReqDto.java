package io.github.xxyopen.novel.book.dto.req;

import io.github.xxyopen.novel.common.req.PageReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BookPageReqDto  extends PageReqDto {

    /**
     * 作家ID
     */
    @Schema(description = "作家ID", required = true)
    private Long authorId;

}
