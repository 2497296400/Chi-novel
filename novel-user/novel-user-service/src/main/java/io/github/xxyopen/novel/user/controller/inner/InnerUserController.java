package io.github.xxyopen.novel.user.controller.inner;

import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import io.github.xxyopen.novel.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "InnerUserController", description = "内部接口-用户模块")
@RestController
@RequestMapping(ApiRouterConsts.API_INNER_USER_URL_PREFIX)
@RequiredArgsConstructor
public class InnerUserController {
    private final UserService userService;

    @Operation(description = "根据Ids查询用户信息")
    @PostMapping("listUserInfoByIds")
    public RestResp<List<UserInfoRespDto>> listUserInfoByIds(@Parameter(description = "用户Id列表") @RequestBody List<Long> ids) {
        return userService.listUserInfoByIds(ids);
    }
}
