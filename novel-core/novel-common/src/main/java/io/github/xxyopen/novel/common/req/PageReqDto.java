package io.github.xxyopen.novel.common.req;

import io.swagger.v3.oas.annotations.Parameter;
import jdk.jfr.Description;
import lombok.Data;

@Data
public class PageReqDto {
    @Parameter(description = "当前页码 默认为 1 ")
    private int pageNum = 1;
    @Parameter(description = "默认为 10 分页页数")
    private int pageSize = 10;
    /*
    * 是否查询所有 默认不查询
    * */
    @Parameter(hidden = true)
    private boolean fetchAll = false;
}
