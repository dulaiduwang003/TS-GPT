package com.cn.app.superbot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * The type Mybatis plus config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
public class MybatisPlusConfig {


    /**
     * Pagination interceptor mybatis plus interceptor.
     *
     * @return the mybatis plus interceptor
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
