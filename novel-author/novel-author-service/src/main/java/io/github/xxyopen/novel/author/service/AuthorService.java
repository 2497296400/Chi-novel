package io.github.xxyopen.novel.author.service;

import io.github.xxyopen.novel.author.dto.req.AuthorRegisterReqDto;
import io.github.xxyopen.novel.common.resp.RestResp;

public interface AuthorService {
    RestResp<Void> register(AuthorRegisterReqDto authorRegisterReqDto);

    RestResp<Integer> getStatus(Long userId);
}
