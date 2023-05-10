package io.github.xxyopen.novel.home.service;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.home.dto.resp.HomeBookRespDto;
import io.github.xxyopen.novel.home.dto.resp.HomeFriendLinkRespDto;

import java.util.List;

/**
 * 首页模块 服务类
 *
 * @author xiongxiaoyang
 * @date 2022/5/13
 */
public interface HomeService {
    RestResp<List<HomeBookRespDto>> listHomeBooks();

    RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks();
}
