package com.cn.app.superbot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.app.superbot.entity.ExchangeCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * The interface Exchange code mapper.
 */
@Mapper
@Repository
public interface ExchangeCodeMapper extends BaseMapper<ExchangeCode> {
}
