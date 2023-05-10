package io.github.xxyopen.novel.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.xxyopen.novel.book.dto.resp.BookChapterRespDto;
import io.github.xxyopen.novel.book.dto.resp.BookInfoRespDto;
import io.github.xxyopen.novel.book.feign.BookFeign;
import io.github.xxyopen.novel.common.auth.JwtUtils;
import io.github.xxyopen.novel.common.auth.UserHolder;
import io.github.xxyopen.novel.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.common.constant.SystemConfigConsts;
import io.github.xxyopen.novel.common.resp.RestResp;
import io.github.xxyopen.novel.config.exception.BusinessException;
import io.github.xxyopen.novel.config.exception.NovelEx;
import io.github.xxyopen.novel.user.dao.entity.UserBookshelf;
import io.github.xxyopen.novel.user.dao.entity.UserFeedback;
import io.github.xxyopen.novel.user.dao.entity.UserInfo;
import io.github.xxyopen.novel.user.dao.mapper.UserBookshelfMapper;
import io.github.xxyopen.novel.user.dao.mapper.UserFeedbackMapper;
import io.github.xxyopen.novel.user.dao.mapper.UserInfoMapper;
import io.github.xxyopen.novel.user.dto.req.UserInfoUptReqDto;
import io.github.xxyopen.novel.user.dto.req.UserLoginReqDto;
import io.github.xxyopen.novel.user.dto.req.UserRegisterReqDto;
import io.github.xxyopen.novel.user.dto.resp.UserInfoRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserLoginRespDto;
import io.github.xxyopen.novel.user.dto.resp.UserRegisterRespDto;
import io.github.xxyopen.novel.user.manager.redis.VerifyCodeManager;
import io.github.xxyopen.novel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInfoMapper userInfoMapper;
    private final VerifyCodeManager verifyCodeManager;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
    /*    if (!verifyCodeManager.imgVerifyCodeOk(dto.getSessionId(), dto.getVelCode())) {
            throw  new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }*/
        Long count = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, dto.getUsername()));
        if (count > 1) {
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }
        verifyCodeManager.removeImgVerifyCode(dto.getSessionId());
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(dto.getUsername());
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setUsername(dto.getUsername());
        userInfo.setSalt("0");
        userInfo.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfoMapper.insert(userInfo);
        return RestResp.ok(UserRegisterRespDto.builder().token(JwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_AUTHOR_KEY)).uid(userInfo.getId()).build());

    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto) {
        String username = userLoginReqDto.getUsername();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        if (userInfo == null) {
            NovelEx.cast("用户名或密码错误");
        }
        String passWord = DigestUtils.md5DigestAsHex(userLoginReqDto.getPassword().getBytes(StandardCharsets.UTF_8));

        if (!userInfo.getPassword().equals(passWord)) {
            NovelEx.cast("用户名或密码错误");
        }
        return RestResp.ok(UserLoginRespDto.builder().token(JwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_AUTHOR_KEY)).uid(userInfo.getId()).nickName(userInfo.getNickName()).build());

    }

    @Override
    public RestResp<UserInfoRespDto> getUserInfo(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return RestResp.ok(UserInfoRespDto.builder().userPhoto(userInfo.getUserPhoto())
                .userSex(userInfo.getUserSex())
                .nickName(userInfo.getNickName())
                .username(userInfo.getUsername()).build());
    }

    @Override
    public RestResp<Void> updateUserInfo(UserInfoUptReqDto userInfoUptReqDto) {
        UserInfo userInfo = userInfoMapper.selectById(UserHolder.getUserId());
        BeanUtils.copyProperties(userInfoUptReqDto, userInfo);
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfoMapper.updateById(userInfo);
        return RestResp.ok();
    }

    private final UserFeedbackMapper userFeedbackMapper;

    @Override
    public RestResp<Void> submitFeedback(Long userId, String conent) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setCreateTime(LocalDateTime.now());
        userFeedback.setUpdateTime(LocalDateTime.now());
        userFeedback.setContent(conent);
        userFeedbackMapper.insert(userFeedback);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteFeedback(Long id) {
        Long userId = UserHolder.getUserId();
        userFeedbackMapper.delete(new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getId, id).eq(UserFeedback::getUserId, userId));
        return RestResp.ok();
    }

    @Override
    public RestResp<List<UserInfoRespDto>> listUserInfoByIds(List<Long> ids) {
        return RestResp.ok(userInfoMapper.selectBatchIds(ids).stream().map(v ->
                UserInfoRespDto.builder()
                        .username(v.getUsername())
                        .userPhoto(v.getUserPhoto())
                        .nickName(v.getNickName())
                        .id(v.getId()).build()
        ).toList());
    }

  private final   UserBookshelfMapper userBookshelfMapper;

    @Override
    public RestResp<Integer> getBookshelfStatus(Long userId, Long bookId) {
        UserBookshelf bookshelf = userBookshelfMapper.selectOne(new LambdaQueryWrapper<UserBookshelf>().eq(UserBookshelf::getBookId, bookId).eq(UserBookshelf::getUserId, userId));
        if (bookshelf != null) {
            return RestResp.ok(0);
        }
        return RestResp.ok(1);
    }

    private final BookFeign bookFeign;

    @Override
    public RestResp<Void> addBookshelf(Long chapterId) {
        Long userId = UserHolder.getUserId();
        BookChapterRespDto chapter = bookFeign.getChapterById(chapterId).getData();
        if (chapter != null) {
            Long bookId = chapter.getBookId();
            UserBookshelf bookshelf = userBookshelfMapper.selectOne(new LambdaQueryWrapper<UserBookshelf>().eq(UserBookshelf::getBookId, bookId).eq(UserBookshelf::getUserId, userId));
            if (Objects.isNull(bookshelf)) {
                bookshelf = new UserBookshelf();
                bookshelf.setUserId(userId);
                bookshelf.setCreateTime(LocalDateTime.now());
                bookshelf.setUpdateTime(LocalDateTime.now());
                bookshelf.setBookId(bookId);
                bookshelf.setPreContentId(chapterId);
                userBookshelfMapper.insert(bookshelf);
                return RestResp.ok();
            }
            bookshelf.setPreContentId(chapterId);
            bookshelf.setUpdateTime(LocalDateTime.now());
            userBookshelfMapper.updateById(bookshelf);
        }
        return RestResp.ok();
    }
}