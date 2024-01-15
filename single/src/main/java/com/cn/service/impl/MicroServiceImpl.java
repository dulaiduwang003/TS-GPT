package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cn.constant.MicroAppConstant;
import com.cn.dto.*;
import com.cn.entity.TsMicroApp;
import com.cn.entity.TsMicroCategory;
import com.cn.mapper.TsMicroAppMapper;
import com.cn.mapper.TsMicroCategoryMapper;
import com.cn.service.MicroService;
import com.cn.utils.RedisUtils;
import com.cn.utils.StringUtils;
import com.cn.vo.MicroAppVo;
import com.cn.vo.MicroCategoryPageVo;
import com.cn.vo.MicroCategoryVo;
import com.cn.vo.MicroPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 预设词 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MicroServiceImpl implements MicroService {

    private final TsMicroCategoryMapper tsMicroCategoryMapper;

    private final TsMicroAppMapper tsMicroAppMapper;

    private final RedisUtils redisUtils;
    private final static int ALL_APP_PAGE_SIZE = 2;
    private final static int SEARCH_APP_PAGE_SIZE = 10;

    @Override
    public IPage<MicroAppVo> getMicroAppPage(final int pageNum) {

        return tsMicroCategoryMapper.selectPage(new Page<>(pageNum, ALL_APP_PAGE_SIZE), new QueryWrapper<TsMicroCategory>()
                .lambda()
                .select(TsMicroCategory::getCategoryName, TsMicroCategory::getMicroCategoryId, TsMicroCategory::getElIcon)).convert(t -> {
            final MicroAppVo vo = new MicroAppVo();
            //填充名字
            vo.setCategoryName(t.getCategoryName());
            //填充ICON
            vo.setElIcon(t.getElIcon());
            final List<TsMicroApp> applications = tsMicroAppMapper.selectList(new QueryWrapper<TsMicroApp>()
                    .lambda()
                    .eq(TsMicroApp::getMicroCategoryId, t.getMicroCategoryId())
                    .select(TsMicroApp::getMicroAppId,
                            TsMicroApp::getTitle,
                            TsMicroApp::getIntroduce,
                            TsMicroApp::getIcon,
                            TsMicroApp::getChineseIssue,
                            TsMicroApp::getEnglishIssue,
                            TsMicroApp::getChineseAnswer,
                            TsMicroApp::getEnglishAnswer
                    )
            );
            List<MicroAppVo.Vo> list;
            //处理子集数据
            if (applications.size() > 5) {
                list = applications.parallelStream().map(this::handleMicroApplicationVo).toList();
                //正常处理
            } else list = applications.stream().map(this::handleMicroApplicationVo).toList();


            return vo.setList(list);
        });


    }

    /**
     * 转化视图 并设置阅读量
     *
     * @param e the ts micro applications
     * @return the micro application vo . vo
     */
    private MicroAppVo.Vo handleMicroApplicationVo(final TsMicroApp e) {
        final MicroAppVo.Vo s = new MicroAppVo.Vo()
                .setMicroAppId(e.getMicroAppId())
                .setIntroduce(e.getIntroduce())
                .setIcon(e.getIcon())
                .setChineseIssue(e.getChineseIssue())
                .setEnglishIssue(e.getEnglishIssue())
                .setIntroduce(e.getIntroduce())
                .setChineseAnswer(e.getChineseAnswer())
                .setEnglishAnswer(e.getEnglishAnswer())
                .setTitle(e.getTitle());
        //获取阅读量
        final Object value = redisUtils.getValue(MicroAppConstant.MICRO_VISITS + e.getMicroAppId());
        if (value != null) {
            s.setVisits(Long.parseLong(String.valueOf(value)));
        } else s.setVisits(0L);
        return s;
    }

    @Override
    public IPage<MicroAppVo.Vo> searchMicroApp(final String prompt, final int pageNum) {

        return tsMicroAppMapper.selectPage(new Page<>(pageNum, SEARCH_APP_PAGE_SIZE), new QueryWrapper<TsMicroApp>()
                        .lambda()
                        .like(TsMicroApp::getTitle, prompt)
                        .or()
                        .like(TsMicroApp::getIntroduce, prompt)
                        .select(TsMicroApp::getTitle,
                                TsMicroApp::getIcon,
                                TsMicroApp::getMicroAppId,
                                TsMicroApp::getIntroduce,
                                TsMicroApp::getChineseIssue,
                                TsMicroApp::getEnglishIssue,
                                TsMicroApp::getChineseAnswer,
                                TsMicroApp::getEnglishAnswer
                        ))
                .convert(this::handleMicroApplicationVo);


    }

    @Override
    public void deleteMicroCategory(final DeleteMicroCategoryDto dto) {
        tsMicroAppMapper.delete(new QueryWrapper<TsMicroApp>()
                .lambda()
                .eq(TsMicroApp::getMicroCategoryId, dto.getMicroCategoryId())
        );

        tsMicroCategoryMapper.delete(new QueryWrapper<TsMicroCategory>()
                .lambda()
                .eq(TsMicroCategory::getMicroCategoryId, dto.getMicroCategoryId())
        );
    }

    @Override
    public void updateMicroCategory(UpdateMicroCategoryDto dto) {
        tsMicroCategoryMapper.updateById(
                new TsMicroCategory()
                        .setMicroCategoryId(dto.getMicroCategoryId())
                        .setCategoryName(dto.getCategoryName())
                        .setElIcon(dto.getElIcon())
        );
    }

    @Override
    public void addMicroCategory(AddMicroCategoryDto dto) {
        tsMicroCategoryMapper.insert(
                new TsMicroCategory()
                        .setCategoryName(dto.getCategoryName())
                        .setElIcon(dto.getElIcon())
        );
    }

    @Override
    public void addMicroApp(AddMicroAppDto dto) {
        tsMicroAppMapper.insert(new TsMicroApp()
                .setMicroCategoryId(dto.getMicroCategoryId())
                .setIcon(dto.getIcon())
                .setTitle(dto.getTitle())
                .setIntroduce(dto.getIntroduce())
                .setChineseIssue(dto.getChineseIssue())
                .setEnglishIssue(dto.getEnglishIssue())
                .setChineseAnswer(dto.getChineseAnswer())
                .setEnglishAnswer(dto.getEnglishAnswer())
                .setMicroCategoryId(dto.getMicroCategoryId())
        );
    }

    @Override
    public List<MicroCategoryVo> getMicroCategoryList() {
        return tsMicroCategoryMapper.selectList(null).parallelStream().map(c -> new MicroCategoryVo().setMicroCategoryId(c.getMicroCategoryId()).setCategoryName(c.getCategoryName())).toList();

    }

    @Override
    public void updateMicroApp(UpdateMicroAppDto dto) {
        tsMicroAppMapper.updateById(new TsMicroApp()
                .setMicroAppId(dto.getMicroAppId())
                .setMicroCategoryId(dto.getMicroCategoryId())
                .setIcon(dto.getIcon())
                .setTitle(dto.getTitle())
                .setIntroduce(dto.getIntroduce())
                .setChineseIssue(dto.getChineseIssue())
                .setEnglishIssue(dto.getEnglishIssue())
                .setChineseAnswer(dto.getChineseAnswer())
                .setEnglishAnswer(dto.getEnglishAnswer())
                .setMicroCategoryId(dto.getMicroCategoryId())
        );
    }

    @Override
    public void deleteMicroApp(DeleteMicroAppDto dto) {
        tsMicroAppMapper.deleteById(new TsMicroApp().setMicroAppId(dto.getMicroAppId()));
    }

    @Override
    public void useMicroApp(final UseMicroTemplateDto dto) {
        redisUtils.increment(MicroAppConstant.MICRO_VISITS + dto.getMicroAppId(), 1);
    }

    @Override
    public IPage<MicroCategoryPageVo> getMicroCategory(int pageNum, String prompt) {

        return tsMicroCategoryMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsMicroCategory>()
                .lambda()
                .like(StringUtils.notEmpty(prompt), TsMicroCategory::getCategoryName, prompt)
        ).convert(s -> new MicroCategoryPageVo()
                .setCategoryName(s.getCategoryName())
                .setMicroCategoryId(s.getMicroCategoryId())
                .setElIcon(s.getElIcon())
                .setUpdateTime(s.getUpdateTime())
                .setCreatedTime(s.getCreatedTime()));

    }

    @Override
    public IPage<MicroPageVo> getMicroAppPage(int pageNum, String prompt) {
        return tsMicroAppMapper.selectPage(new Page<>(pageNum, 15),
                new QueryWrapper<TsMicroApp>()
                        .lambda()
                        .like(StringUtils.notEmpty(prompt), TsMicroApp::getTitle, prompt)
        ).convert(t -> {

            final TsMicroCategory tsMicroCategory = tsMicroCategoryMapper.selectOne(new QueryWrapper<TsMicroCategory>().lambda()
                    .eq(TsMicroCategory::getMicroCategoryId, t.getMicroCategoryId())
                    .select(TsMicroCategory::getCategoryName)
            );


            return new MicroPageVo()
                    .setMicroAppId(t.getMicroAppId())
                    .setIcon(t.getIcon())
                    .setTitle(t.getTitle())
                    .setIntroduce(t.getIntroduce())
                    .setCreatedTime(t.getCreatedTime())
                    .setMicroCategoryId(t.getMicroCategoryId())
                    .setUpdateTime(t.getUpdateTime())
                    .setCategoryName(tsMicroCategory.getCategoryName())
                    .setChineseAnswer(t.getChineseAnswer())
                    .setEnglishAnswer(t.getEnglishAnswer())
                    .setEnglishIssue(t.getEnglishIssue())
                    .setChineseIssue(t.getChineseIssue());
        });
    }
}
