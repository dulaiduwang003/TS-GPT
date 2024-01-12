package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.UpdateUserInfoDto;
import com.cn.dto.UploadUserNickNameDto;
import com.cn.vo.UserInfoVo;
import com.cn.vo.UserPageVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface UserService {


    /**
     * 获取当前登录用户信息
     *
     * @return the current user info
     */
    UserInfoVo getCurrentUserInfo();


    /**
     * 更新用户头像
     *
     * @param file the file
     */
    void uploadAvatar(final MultipartFile file);


    /**
     * 修改用户昵称
     *
     * @param dto the dto
     */
    void uploadNickName(final UploadUserNickNameDto dto);


    /**
     * 更新用户信息数据
     *
     * @param dto the dto
     */
    void updateUserInfo(UpdateUserInfoDto dto);


    /**
     * 分页获取用户列表数据
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the user page
     */
    IPage<UserPageVo> getUserPage(final int pageNum, final String prompt);
}
