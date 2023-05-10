package io.github.xxyopen.novel.book.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCategoryRespDto {
    @Schema(description = "类别id")
    private Long id;
    
    @Schema(description = "类别名称")
    private String name;
    
}
