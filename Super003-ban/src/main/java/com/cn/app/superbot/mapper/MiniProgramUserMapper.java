package com.cn.app.superbot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.app.superbot.entity.MiniProgramUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * The interface Mini program user mapper.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Mapper
@Repository
public interface MiniProgramUserMapper extends BaseMapper<MiniProgramUser> {
}
