package com.cn.app.superbot.service;

import com.cn.app.superbot.config.ServerConfig;
import com.cn.app.superbot.dto.ChooseTacticsDto;
import com.cn.app.superbot.dto.PostNoticesDto;
import com.cn.app.superbot.dto.PutServerConfigDto;
import com.cn.app.superbot.vo.CodeVo;

import java.util.List;

/**
 * The interface Server service.
 *
 * @author bdth
 */
public interface ServerService {

    /**
     * Put server config.
     *
     * @param dto the dto
     * @return the server config
     */
    ServerConfig putServerConfig(final PutServerConfigDto dto);


    /**
     * Choose tactics.
     *
     * @param dto the dto
     * @author bdth
     */
    void chooseTactics(final ChooseTacticsDto dto);

    /**
     * Gets server config.
     *
     * @return the server config
     */
    ServerConfig getServerConfig();


    /**
     * Generate.
     *
     * @param frequency the frequency
     * @param quantity  the quantity
     */
    void generate(final Long frequency, final Long quantity);


    /**
     * Gets codes.
     *
     * @return the codes
     */
    List<CodeVo> getCodes();


    /**
     * Post notices.
     *
     * @param dto the dto
     */
    void postNotices(final PostNoticesDto dto);

}
