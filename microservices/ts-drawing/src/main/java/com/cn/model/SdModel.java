package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * The type Sd model.
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class SdModel implements Serializable {

    /**
     * The Init images.
     */
    private List<String> init_images;

    /**
     * The Mask.
     */
    private String mask;
//
//    private Double denoising_strength;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Width.
     */
    private Long width;

    /**
     * The Height.
     */
    private Long height;

    /**
     * The Sampler index.
     */
    private String sampler_index;

    /**
     * The Steps.
     */
    private Integer steps;

    /**
     * The Negative prompt.
     */
    private String negative_prompt;

    /**
     * The Override settings.
     */
    private Override override_settings;

    /**
     * The type Override.
     */
    @Data
    @Accessors(chain = true)
    public static class Override implements Serializable {

        /**
         * The Sd model checkpoint.
         */
        private String sd_model_checkpoint;

        /**
         * The Sd vae.
         */
        private String sd_vae;
    }

}
