package com.cn.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.constant.UserCacheConstant;
import com.cn.dto.UpdateUserInfoDto;
import com.cn.dto.UploadUserNickNameDto;
import com.cn.entity.TsUser;
import com.cn.enums.FileEnum;
import com.cn.mapper.TsUserMapper;
import com.cn.service.UserService;
import com.cn.structure.UserInfoStructure;
import com.cn.utils.StringUtils;
import com.cn.utils.UploadUtil;
import com.cn.utils.UserUtils;
import com.cn.vo.UserInfoVo;
import com.cn.vo.UserPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 用户业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final TsUserMapper tsUserMapper;

    private final UploadUtil uploadUtil;

    @Override
    public UserInfoVo getCurrentUserInfo() {
        //获取当前登录用户缓存数据(包含头像)
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        //目标过期时间
        final LocalDateTime expirationTime = currentUserInfo.getExpirationTime();
        //获取系统当前时间
        final LocalDateTime currentTime = LocalDateTime.now();

        final boolean isMember = expirationTime != null && !expirationTime.isBefore(currentTime);

        return new UserInfoVo()
                .setNickName(currentUserInfo.getNickName())
                .setMember(new UserInfoVo.Member().setIsMember(isMember).setExpirationTime(expirationTime))
                .setType(currentUserInfo.getType())
                .setAvatar(currentUserInfo.getAvatar());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadAvatar(final MultipartFile file) {
        final String newAvatar = uploadUtil.uploadFile(file, FileEnum.AVATAR.getDec());
        //当前用户ID
        final Long currentLoginId = UserUtils.getCurrentLoginId();
        //获取用户真实头像地址
        final TsUser tsUser = tsUserMapper.selectOne(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getUserId, currentLoginId)
                .select(TsUser::getAvatar,TsUser::getUserId)
        );
        final String oldAvatar = tsUser.getAvatar();
        //修改头像
        tsUserMapper
                .updateById(
                        tsUser
                                .setAvatar(newAvatar)
                                .setUserId(currentLoginId)
                );
        //获取原有的用户数据信息 仅更新一次头像即可
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        //更新头像
        currentUserInfo.setAvatar(newAvatar);
        StpUtil.getSession()
                //设置用户数据缓存
                .set(UserCacheConstant.USER_INFO_DATA, currentUserInfo);
        if (!StringUtils.isEmpty(oldAvatar)) {
            uploadUtil.deletedFile(oldAvatar);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(final UpdateUserInfoDto dto) {

        tsUserMapper.updateById(
                new TsUser().setUserId(dto.getUserId())
                        .setExpirationTime(dto.getExpirationTime())
                        .setType(dto.getType())
                        .setNickName(dto.getNickName())
        );

        //用户可能推出登录
        if (StpUtil.isLogin(dto.getUserId())) {
            //获取原有的用户数据信息
            final UserInfoStructure currentUserInfo = UserUtils.getUserInfoById(dto.getUserId());
            if (currentUserInfo != null) {
                //更新用户登录缓存信息
                StpUtil.getSessionByLoginId(dto.getUserId())
                        .set(UserCacheConstant.USER_INFO_DATA,
                                currentUserInfo.setType(dto.getType())
                                        .setNickName(dto.getNickName())
                                        .setExpirationTime(dto.getExpirationTime())
                        );
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadNickName(final UploadUserNickNameDto dto) {
        tsUserMapper.updateById(new TsUser().setNickName(dto.getNickName()).setUserId(UserUtils.getCurrentLoginId()));
        //获取原有的用户数据信息
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        //更新头像
        currentUserInfo.setNickName(dto.getNickName());
        StpUtil.getSession()
                //设置用户数据缓存
                .set(UserCacheConstant.USER_INFO_DATA, currentUserInfo);
    }

    @Override
    public IPage<UserPageVo> getUserPage(final int pageNum, final String prompt) {

        return tsUserMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsUser>()
                .lambda()
                .like(StringUtils.notEmpty(prompt), TsUser::getEmail, prompt)
                .or()
                .like(StringUtils.notEmpty(prompt), TsUser::getNickName, prompt)
                .select(TsUser::getUserId, TsUser::getType, TsUser::getNickName, TsUser::getEmail, TsUser::getExpirationTime, TsUser::getCreatedTime, TsUser::getUpdateTime)
        ).convert(t -> {
            //判断是否为会员身份
            final LocalDateTime expirationTime = t.getExpirationTime();

            boolean isMember = false;

            Integer timeBetweenThem = null;

            if (t.getExpirationTime() != null) {
                final LocalDateTime now = LocalDateTime.now();
                isMember = expirationTime.isAfter(now);
                //如果是会员 计算 还有多少天到期
                if (isMember) {
                    timeBetweenThem = Math.toIntExact(ChronoUnit.DAYS.between(now, expirationTime));
                }
            }

            return new UserPageVo()
                    .setNickName(t.getNickName())
                    .setUserId(t.getUserId())
                    .setEmail(t.getEmail())
                    .setType(t.getType())
                    .setDaysRemaining(timeBetweenThem)
                    .setExpirationTime(expirationTime)
                    .setCreatedTime(t.getCreatedTime())
                    .setUpdateTime(t.getUpdateTime())
                    .setIsMember(isMember);

        });

    }
}
