package com.cn.app.superbot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.RedisLockConstants;
import com.cn.app.superbot.constants.UserConstants;
import com.cn.app.superbot.entity.ExchangeCode;
import com.cn.app.superbot.entity.MiniProgramUser;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.mapper.ExchangeCodeMapper;
import com.cn.app.superbot.mapper.MiniProgramUserMapper;
import com.cn.app.superbot.service.UserService;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.RedisLockHelper;
import com.cn.app.superbot.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User service.
 *
 * @author bdth
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * The Mini program user mapper.
     */
    private final MiniProgramUserMapper miniProgramUserMapper;


    /**
     * The Code mapper.
     */
    private final ExchangeCodeMapper codeMapper;


    /**
     * The Lock helper.
     */
    private final RedisLockHelper lockHelper;

    /**
     * Gets user info.
     *
     * @return the user info
     */
    @Override
    public UserInfoVo getUserInfo() {
        final int id = Integer.parseInt(StpUtil.getLoginId().toString());
        try {
            return BeanUtils.copyClassProperTies(
                    miniProgramUserMapper
                            .selectOne(new QueryWrapper<MiniProgramUser>()
                                    .lambda()
                                    .eq(MiniProgramUser::getMiniProgramUserId, id)
                                    .select(MiniProgramUser::getOpenId, MiniProgramUser::getFrequency)
                            ),
                    UserInfoVo.class);
        } catch (NullPointerException e) {
            throw new CustomException(MsgConstants.USER_INFO_ERR, 400);
        }
    }

    /**
     * Uses frequency integer.
     *
     * @return the integer
     */
    @Override
    public MiniProgramUser usesFrequencyInfo() {
        final int id = Integer.parseInt(StpUtil.getLoginId().toString());
        return miniProgramUserMapper.selectOne(new QueryWrapper<MiniProgramUser>()
                .lambda().eq(MiniProgramUser::getMiniProgramUserId, id)
                .select(MiniProgramUser::getFrequency, MiniProgramUser::getMiniProgramUserId)
        );

    }

    /**
     * Decrease frequency.
     *
     * @param id the id
     */
    @Override
    public void decreaseFrequency(final Long id, final Long frequency) {
        miniProgramUserMapper.updateById(
                new MiniProgramUser().setMiniProgramUserId(id)
                        .setFrequency(frequency)
        );
    }


    /**
     * Gets rewards.
     *
     * @param type the type
     * @return the rewards
     */
    @Override
    public UserInfoVo getRewards(final Integer type) {
        try {
            final int id = Integer.parseInt(StpUtil.getLoginId().toString());
            final MiniProgramUser _m = miniProgramUserMapper.selectOne(new QueryWrapper<MiniProgramUser>()
                    .lambda()
                    .eq(MiniProgramUser::getMiniProgramUserId, id)
                    .select(MiniProgramUser::getFrequency, MiniProgramUser::getMiniProgramUserId, MiniProgramUser::getOpenId, MiniProgramUser::getShare, MiniProgramUser::getVideo)
            );
            // 0 denote video  : 1 denote share
            if (type == 0) {
                if (_m.getVideo() >= 1) {
                    _m
                      .setFrequency(_m.getFrequency() + UserConstants.REWARD_FREQUENCY)
                      .setVideo(_m.getVideo() - UserConstants.DEDUCT);
                }
                // 0 denote video  : 1 denote share
            } else if (_m.getShare() == 0) {
                _m
                        .setFrequency(_m.getFrequency() + UserConstants.SHARE_FREQUENCY)
                        .setShare(1L);
            }
            miniProgramUserMapper.updateById(_m);
            //toVO
            return BeanUtils.copyClassProperTies(_m, UserInfoVo.class);
        } catch (Exception e) {
            throw new CustomException(MsgConstants.REWARD_API_ERR, 500);
        }
    }

    /**
     * Use code.
     *
     * @param code the code
     */
    @Override
    public void useCode(final String code) {

        final ExchangeCode exchangeCode = codeMapper.selectOne(new QueryWrapper<ExchangeCode>()
                .lambda()
                .eq(ExchangeCode::getCode, code)
                .select(ExchangeCode::getExchangeCodeId, ExchangeCode::getFrequency)
                .last("limit 1")
        );
        if (exchangeCode != null) {
            // lock time
            final String lockTime = String.valueOf(System.currentTimeMillis());
            //lock prefix
            final String prefix = RedisLockConstants.EXCHANGE_CODE_KEY + exchangeCode.getCode();

            try {

                final boolean lock = lockHelper.lock(prefix, lockTime);
                if (!lock) {
                    throw new CustomException(MsgConstants.EXCHANGE_CODE_LOCK_ERR, 400);
                }
                //get user id
                final int id = Integer.parseInt(StpUtil.getLoginId().toString());

                final MiniProgramUser _m = miniProgramUserMapper.selectOne(new QueryWrapper<MiniProgramUser>()
                        .lambda()
                        .eq(MiniProgramUser::getMiniProgramUserId, id)
                        .select(MiniProgramUser::getMiniProgramUserId, MiniProgramUser::getFrequency)
                        .last("limit 1")
                );

                miniProgramUserMapper.updateById(_m.setFrequency(_m.getFrequency() + exchangeCode.getFrequency()));

                codeMapper.deleteById(
                        //card number
                        exchangeCode.getExchangeCodeId()
                );
            } finally {
                lockHelper.unlock(prefix, lockTime);
            }

        } else {
            throw new CustomException(MsgConstants.EXCHANGE_CODE_NULL_ERR, 400);
        }
    }
}
