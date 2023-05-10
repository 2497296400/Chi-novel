package io.github.xxyopen.novel.book.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author 41038
 * 小说内容返回Dto
 */
@Data
@Builder
public class BookContentAboutRespDto {
    @Schema(description = "小说信息")
    private BookInfoRespDto bookInfo;

    @Schema(description = "小说章节信息")
    private BookChapterRespDto chapteInfo;

    @Schema(description = "小说内容")
    private String bookContent;
    
}
