package com.cn.app.superbot.service;

import com.cn.app.superbot.entity.MiniProgramUser;
import com.cn.app.superbot.vo.UserInfoVo;

/**
 * The interface User service.
 *
 * @author bdth
 */
public interface UserService {

    /**
     * Gets user info.
     *
     * @return the user info
     */
    UserInfoVo getUserInfo();


    /**
     * Uses frequency integer.
     *
     * @return the integer
     */
    MiniProgramUser usesFrequencyInfo();


    /**
     * Decrease frequency.
     *
     * @param id the id
     */
    void decreaseFrequency(final Long id, final Long frequency);


    /**
     * Gets rewards.
     *
     * @param type the type
     * @return the rewards
     */
    UserInfoVo getRewards(final Integer type);


    /**
     * Use code.
     *
     * @param code the code
     */
    void useCode(final String code);
}
