package io.github.xxyopen.novel.user.feign;

import io.github.xxyopen.novel.common.constant.ApiRouterConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Component
@FeignClient(value = "novel-user-service",fallback = UserFeign.UserFeignFallback.class)
public interface UserFeign {
    @PostMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/listUserInfoByIds")
    RestResp<List<UserInfoRespDto>> listUserInfoByIds(List<Long> userId);

    @Component
    class UserFeignFallback implements UserFeign {

        @Override
        public RestResp<List<UserInfoRespDto>> listUserInfoByIds(List<Long> userId) {
            return RestResp.ok(new ArrayList<>(0));
        }
    }
}
