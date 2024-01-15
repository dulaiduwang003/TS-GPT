package com.cn.init;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.entity.TsUser;
import com.cn.enums.RoleEnum;
import com.cn.mapper.TsUserMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 初始化auth服务
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class InitCommon {

    private final TsUserMapper tsUserMapper;

    @Value("${spring.mail.username}")
    private String email;


    /**
     * 构建默认管理员账户
     */
    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        TsUser tsUser = tsUserMapper.selectOne(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getEmail, email)
        );
        if (tsUser == null) {
            tsUser = new TsUser()
                    .setType(RoleEnum.ADMIN.getDesc())
                    .setEmail(email);
            tsUserMapper.insert(tsUser);
            return;
        }
        tsUserMapper.updateById(tsUser.setType(RoleEnum.ADMIN.getDesc()));
    }
}
