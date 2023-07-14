package com.cn.app.superbot.api;

import com.cn.app.superbot.annotations.BlockKeywords;
import com.cn.app.superbot.dto.*;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.model.FaceModel;
import com.cn.app.superbot.model.GptAlphaModel;
import com.cn.app.superbot.model.GptFourModel;
import com.cn.app.superbot.model.GptTurboModel;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.ChatService;
import com.cn.app.superbot.utils.DivisionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Gpt ability api.
 *
 * @author bdth
 */
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ChatApi {

    /**
     * The Gpt service.
     */
    private final ChatService chatService;


    /**
     * Completions result.
     *
     * @param dto the dto
     * @return the result
     */

    @BlockKeywords
    @PostMapping(value = "/chat/completions", name = "GPT-Turbo 3.5", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result completions(@Validated @RequestBody final GptTurboDto dto) {
        try {
            final GptTurboModel model = GptTurboDto.convertToGptTurboModel(DivisionUtils.turboDivision(dto));
            return Result.data(chatService.build(model, "chat/completions"));
        } catch (CustomException e) {
            log.error("Failed to fetch GPT3.5 model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * Intensifier result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords(limit = 4)
    @PostMapping(value = "/chat/intensifier", name = "GPT-4", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result intensifier(@Validated @RequestBody final GptFourDto dto) {
        try {
            final GptFourModel model = GptFourDto.convertToGptFourModel(DivisionUtils.fullDivision(dto));
            return Result.data(chatService.chatGpt4(model));
        } catch (CustomException e) {
            log.error("Failed to fetch GPT4 model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * Image recognition result.
     *
     * @param file the file
     * @return the result
     */
    @BlockKeywords(limit = 5)
    @PostMapping(value = "/chat/recognition", name = "image recognition", consumes = "multipart/form-data")
    public Result imageRecognition(@RequestParam("file") MultipartFile file) {
        try {
            return Result.data(chatService.imageRecognition(file));
        } catch (CustomException e) {
            log.error("Failed to fetch imageRecognition model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * Translation result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords
    @PostMapping(value = "/chat/translation", name = "GPT-translation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result translation(@Validated @RequestBody final GptTurboDto dto) {
        try {
            final GptTurboModel model = GptTurboDto.convertToGptTurboModel(dto);
            return Result.data(chatService.build(model, "chat/completions"));
        } catch (CustomException e) {
            log.error("Failed to fetch GPT3.5 model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * Code result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords
    @PostMapping(value = "/chat/code", name = "GPT-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result code(@Validated @RequestBody final GptTurboDto dto) {
        try {
            final GptTurboModel model = GptTurboDto.convertToGptTurboModel(dto);
            return Result.data(chatService.build(model, "chat/completions"));
        } catch (CustomException e) {
            log.error("Failed to fetch GPT3.5 model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * New bing result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords
    @PostMapping(value = "/chat/bing", name = "new-bing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result newBing(@Validated @RequestBody final NewBingDto dto) {
        try {
            return Result.data(chatService.bing(dto.getParameter()));
        } catch (CustomException e) {
            log.error("Failed to fetch bing model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * Generations result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords(limit = 5, check = false)
    @PostMapping(value = "/images/generations", name = "GPT-Alpha", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result generations(@Validated @RequestBody final GptAlphaDto dto) {
        try {
            final GptAlphaModel model = GptAlphaDto.convertToGptAlphaModel(dto);
            return Result.data(chatService.build(model, "images/generations"));
        } catch (CustomException e) {
            log.error("Failed to fetch GPT-Alpha model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * Face result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords(limit = 10, check = false)
    @PostMapping(value = "/images/face", name = "SD-face", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result face(@Validated @RequestBody final FaceDto dto) {
        try {
            final FaceModel faceModel = FaceDto.convertToFaceModel(dto);
            return Result.data(chatService.face(faceModel));
        } catch (CustomException e) {
            log.error("Failed to fetch SD-face model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }
}
