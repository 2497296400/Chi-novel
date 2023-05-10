package io.github.xxyopen.novel.common.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class PageRespDto<T>  {
    private final long pageNum;

    private final long pageSize;

    private final long total;
    private final List<? extends T> list;

    public static <T> PageRespDto<T> of(long pageNum, long pageSize, long total, List<T> list) {
        return new PageRespDto<>(pageNum, pageSize, total, list);
    }

    public PageRespDto(long pageNum, long pageSize, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }
    public long getPages() {
        if (this.pageSize == 0) {
            return 0;
        }
        return (this.total + this.pageSize - 1) / this.pageSize;
    }
    
}
