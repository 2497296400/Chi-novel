package io.github.xxyopen.novel.book.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
@Data
public class ChapterAddReqDto {
    /**
     * 小说ID
     */
    private Long bookId;

    /**
     * 作家ID
     */
    private Long authorId;

    /**
     * 章节名
     */
    @NotBlank
    @Schema(description = "章节名", required = true)
    private String chapterName;

    /**
     * 章节内容
     */
    @Schema(description = "章节内容", required = true)
    @NotBlank
    @Length(min = 50)
    private String chapterContent;
    
    @Schema(description = "是否收费 1 -收费 0-免费",required = true)
    @NotNull
    private  Integer isVip;
}
