package io.github.xxyopen.novel.user.service;

import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.user.dto.req.UserInfoUptReqDto;
import io.github.xxyopen.novel.user.dto.req.UserLoginReqDto;
import io.github.xxyopen.novel.user.dto.req.UserRegisterReqDto;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserLoginRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserRegisterRespDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    RestResp<UserRegisterRespDto> register( UserRegisterReqDto dto);

    RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto);

    RestResp<UserInfoRespDto> getUserInfo(Long userId);

    RestResp<Void> updateUserInfo(UserInfoUptReqDto userInfoUptReqDto);

    RestResp<Void> submitFeedback(Long userId, String conent);

    RestResp<Void> deleteFeedback(Long id);

    RestResp<List<UserInfoRespDto>> listUserInfoByIds(List<Long> ids);

    RestResp<Integer> getBookshelfStatus(Long userId, Long bookId);

    RestResp<Void> addBookshelf(Long chapterId);
}
