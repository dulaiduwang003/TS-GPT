package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type We cha qr code model.
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class WeChaQrCodeModel implements Serializable {

    /**
     * The Scene.
     */
    private String scene;

    /**
     * The Page.
     */
    private String page = "pages/web/authorizationView";

    /**
     * The Check path.
     */
    private Boolean check_path = false;

    /**
     * The Env version.
     */
    private String env_version = "develop";

    /**
     * The Is hyaline.
     */
    private Boolean is_hyaline = true;

//    private Boolean auto_color = false;
//
//    private LineColor line_color = new LineColor();


//    @Data
//    private static class LineColor {
//        private Integer r = 151;
//
//        private Integer g = 136;
//
//        private Integer b = 239;
//
//    }
}
