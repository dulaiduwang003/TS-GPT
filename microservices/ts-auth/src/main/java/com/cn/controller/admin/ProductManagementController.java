package com.cn.controller.admin;

import com.cn.dto.AddProductCardDto;
import com.cn.dto.DeleteProductCardDto;
import com.cn.dto.UpdateProductCardDto;
import com.cn.msg.Result;
import com.cn.service.ProductCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/product-management")
@RequiredArgsConstructor
public class ProductManagementController {


    private final ProductCardService productCardService;


    /**
     * 分页获取商品信息
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the product page
     */
    @GetMapping(value = "/get/product/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getProductPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam final String prompt) {

        return Result.data(productCardService.getProductCardPage(pageNum, prompt));

    }


    /**
     * email account login
     */
    @PostMapping(value = "/delete/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteProduct(@RequestBody @Validated DeleteProductCardDto dto) {
        productCardService.deleteProductCard(dto);
        return Result.ok();
    }


    /**
     * email account login
     */
    @PostMapping(value = "/add/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addProduct(@RequestBody @Validated AddProductCardDto dto) {
        productCardService.addProductCard(dto);
        return Result.ok();
    }


    /**
     * Update user result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateProduct(@RequestBody @Validated UpdateProductCardDto dto) {
        productCardService.updateProductCard(dto);
        return Result.ok();


    }


}
