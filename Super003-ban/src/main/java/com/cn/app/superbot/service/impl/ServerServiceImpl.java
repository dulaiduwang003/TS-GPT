package com.cn.app.superbot.service.impl;

import com.cn.app.superbot.config.ServerConfig;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.ServerConstants;
import com.cn.app.superbot.dto.ChooseTacticsDto;
import com.cn.app.superbot.dto.PostNoticesDto;
import com.cn.app.superbot.dto.PutServerConfigDto;
import com.cn.app.superbot.entity.ExchangeCode;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.mapper.ExchangeCodeMapper;
import com.cn.app.superbot.service.ServerService;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.RedisUtils;
import com.cn.app.superbot.vo.CodeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * The type Server service.
 *
 * @author bdth
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ServerServiceImpl implements ServerService {

    /**
     * The Redis utils.
     */

    private final RedisUtils redisUtils;


    /**
     * The Characters.
     */
    final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * The Code length.
     */
    final int CODE_LENGTH = 6;

    /**
     * The Random.
     */
    private final Random random = new Random();


    /**
     * The Code mapper.
     */
    private final ExchangeCodeMapper codeMapper;


    /**
     * Post notices.
     *
     * @param dto the dto
     */
    @Override
    public void postNotices(PostNoticesDto dto) {

    }

    /**
     * Put server config.
     *
     * @param dto the dto
     */
    public ServerConfig putServerConfig(final PutServerConfigDto dto) {
        final ServerConfig serverConfig = PutServerConfigDto.convertToServerConfig(dto);
        redisUtils.setValue(ServerConstants.SERVER_CONFIG, serverConfig);
        return getServerConfig();

    }

    /**
     * Generate.
     *
     * @param frequency the frequency
     * @param quantity  the quantity
     */
    @Override
    public void generate(final Long frequency, final Long quantity) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            StringBuilder codeBuilder = new StringBuilder();
            for (int j = 0; j < CODE_LENGTH; j++) {
                int index = random.nextInt(CHARACTERS.length());
                codeBuilder.append(CHARACTERS.charAt(index));
            }
            codes.add(codeBuilder.toString());
        }
        codes.forEach(c -> codeMapper.insert(new ExchangeCode().setCode(c).setFrequency(frequency)));
    }

    /**
     * Gets codes.
     *
     * @return the codes
     */
    @Override
    public List<CodeVo> getCodes() {
        return BeanUtils.copyArrayProperTies(codeMapper.selectList(null), CodeVo.class);
    }

    /**
     * Gets server config.
     *
     * @return the server config
     */
    public ServerConfig getServerConfig() {
        return (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);

    }

    /**
     * Choose tactics.
     *
     * @param dto the dto
     */
    public void chooseTactics(final ChooseTacticsDto dto) {
        final ServerConfig value = (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        try {
            if (value==null){
                throw new CustomException(MsgConstants.CONFIG_ERR, 500);
            }
            final String key = value.getKey();
            switch (dto.getChoose()) {
                //official
                case 1 -> {
                    final ServerConfig.Proxy proxy = value.getProxy();
                    if ((proxy == null) || key == null || proxy.ip == null || proxy.port == null) {
                        throw new CustomException(MsgConstants.NOT_PROXY_ERR, 500);
                    }
                }
                //abroad
                case 2 -> {
                    if (key == null) {
                        throw new CustomException(MsgConstants.OVERSEAS_KEY_ERR, 500);
                    }
                }
                //thirdParty
                case 3 -> {
                    if (value.thirdParty == null || value.thirdParty.baseUrl == null || value.thirdParty.token == null) {
                        throw new CustomException(MsgConstants.THIRD_PARTY_CONFIG_ERR, 500);
                    }
                }
                default -> throw new CustomException(MsgConstants.TACTICS_ERR, 500);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), 500);
        }
        value.setChoose(dto.getChoose());
        redisUtils.setValue(ServerConstants.SERVER_CONFIG, value);
    }

}
