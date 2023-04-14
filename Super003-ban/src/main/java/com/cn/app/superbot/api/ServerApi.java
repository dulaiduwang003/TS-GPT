package com.cn.app.superbot.api;

import com.cn.app.superbot.dto.ChooseTacticsDto;
import com.cn.app.superbot.dto.GenerateCodeDto;
import com.cn.app.superbot.dto.PutServerConfigDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Server control api.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
@Slf4j
public class ServerApi {

    /**
     * The Server service.
     */
    private final ServerService serverService;

    /**
     * Server config result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/put/config", name = "write server configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result putServerConfig(@RequestBody @Validated final PutServerConfigDto dto) {
        try {
            return Result.data(serverService.putServerConfig(dto));
        } catch (CustomException e) {
            log.error("failed to write to server configuration");
            return Result.error();
        }

    }

    /**
     * Gets server config.
     *
     * @return the server config
     */
    @GetMapping(value = "/get/config", name = "get server configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getServerConfig() {
        try {
            return Result.data(serverService.getServerConfig());
        } catch (CustomException e) {
            log.error("failed to get server data");
            return Result.error();
        }

    }

    /**
     * Choose tactics result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/choose/tactics", name = "select tactics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result chooseTactics(@RequestBody final ChooseTacticsDto dto) {
        try {
            serverService.chooseTactics(dto);
            return Result.ok();
        } catch (CustomException e) {
            log.error("failed to select configuration parameters");
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * Gets all redemption codes.
     *
     * @return the all redemption codes
     */
    @GetMapping(value = "/get/code", name = "get codes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAllRedemptionCodes() {
        try {
            return Result.data(serverService.getCodes());
        } catch (CustomException e) {
            log.error("failed to obtain card number");
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * Generate code result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/generate/code", name = "generate code", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result generateCode(@RequestBody @Validated GenerateCodeDto dto) {
        try {
            serverService.generate(dto.getFrequency(), dto.getQuantity());
            return Result.ok();
        } catch (CustomException e) {
            log.error("failed to generate exchange code");
            return Result.error(e.getMessage(), e.getCode());
        }

    }

}
