package io.github.xxyopen.novel.home.controller.front;

import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.home.dto.resp.HomeBookRespDto;
import io.github.xxyopen.novel.home.dto.resp.HomeFriendLinkRespDto;
import io.github.xxyopen.novel.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "HomeController ", description = "前台门口-首页")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "首页小说推荐接口")
    @GetMapping("book")
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return homeService.listHomeBooks();
    }

    @Operation(summary = "友情链接访问接口")
    @GetMapping("friend_Link/list")
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return homeService.listHomeFriendLinks();
    }
}
