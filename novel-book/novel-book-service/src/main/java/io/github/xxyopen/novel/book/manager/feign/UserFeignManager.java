package io.github.xxyopen.novel.book.manager.feign;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import io.github.xxyopen.novel.user.feign.UserFeign;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserFeignManager {
    private final UserFeign userFeign;

    public List<UserInfoRespDto> listUserInfo(List<Long> userId) {
        RestResp<List<UserInfoRespDto>> restResp = userFeign.listUserInfoByIds(userId);
        if (restResp != null) {
            return restResp.getData();
        }
        return new ArrayList<>(0);
    }
}
