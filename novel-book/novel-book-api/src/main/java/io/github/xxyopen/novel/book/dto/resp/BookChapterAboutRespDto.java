package io.github.xxyopen.novel.book.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookChapterAboutRespDto {
    
    private BookChapterRespDto chapterInfo;
    @Schema(description = "章节总数")
    private  Long chapterTotal;
    
    @Schema(description = "内容摘要(30字)")
    private  String contentSummary;
}
