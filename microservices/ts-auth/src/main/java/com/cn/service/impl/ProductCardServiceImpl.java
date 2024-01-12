package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.dto.AddProductCardDto;
import com.cn.dto.DeleteProductCardDto;
import com.cn.dto.UpdateProductCardDto;
import com.cn.entity.TsProductCard;
import com.cn.mapper.TsProductCardMapper;
import com.cn.service.ProductCardService;
import com.cn.utils.StringUtils;
import com.cn.vo.ProductCardPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商品 业务实现
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
@Slf4j
public class ProductCardServiceImpl implements ProductCardService {

    private final TsProductCardMapper tsProductCardMapper;

    @Override
    public IPage<ProductCardPageVo> getProductCardPage(int pageNum, String prompt) {
        return tsProductCardMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsProductCard>()
                .lambda()
                .like(StringUtils.notEmpty(prompt), TsProductCard::getProductName, prompt)
        ).convert(s -> {
            return new ProductCardPageVo()
                    .setProductName(s.getProductName())
                    .setDays(s.getDays())
                    .setPrice(s.getPrice())
                    .setCreatedTime(s.getCreatedTime())
                    .setUpdateTime(s.getUpdateTime())
                    .setProductCardId(s.getProductCardId());
        });

    }

    @Override
    public void deleteProductCard(DeleteProductCardDto dto) {
        tsProductCardMapper.delete(new QueryWrapper<TsProductCard>()
                .lambda()
                .eq(TsProductCard::getProductCardId, dto.getProductCardId())
        );
    }

    @Override
    public void updateProductCard(UpdateProductCardDto dto) {
        tsProductCardMapper.updateById(new TsProductCard()
                .setProductCardId(dto.getProductCardId())
                .setProductName(dto.getProductName())
                .setPrice(dto.getPrice())
                .setDays(dto.getDays())
        );
    }

    @Override
    public void addProductCard(AddProductCardDto dto) {
        tsProductCardMapper.insert(new TsProductCard()
                .setProductName(dto.getProductName())
                .setPrice(dto.getPrice())
                .setDays(dto.getDays())
        );
    }
}
