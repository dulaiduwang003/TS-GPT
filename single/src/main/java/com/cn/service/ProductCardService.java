package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.AddProductCardDto;
import com.cn.dto.DeleteProductCardDto;
import com.cn.dto.UpdateProductCardDto;
import com.cn.vo.ProductCardPageVo;

/**
 * 商品 业务层
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ProductCardService {

    /**
     * 分页获取商品信息
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the product card page
     */
    IPage<ProductCardPageVo> getProductCardPage(final int pageNum, final String prompt);

    /**
     * 删除指定商品
     *
     * @param dto the dto
     */
    void deleteProductCard(DeleteProductCardDto dto);


    /**
     * 修改指定商品
     *
     * @param dto the dto
     */
    void updateProductCard(UpdateProductCardDto dto);

    /**
     * 新增一个商品
     *
     * @param dto the dto
     */
    void addProductCard(AddProductCardDto dto);
}
