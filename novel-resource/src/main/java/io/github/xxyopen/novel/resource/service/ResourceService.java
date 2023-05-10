package io.github.xxyopen.novel.resource.service;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.resource.dto.resp.ImgVerifyCodeRespDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResourceService {
    RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException;

    RestResp<String> uploadImage(MultipartFile file);
}
