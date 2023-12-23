package com.zh.controlcenter.service.impl;

import com.zh.controlcenter.common.apibase.Pager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.controlcenter.service.SysConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.controlcenter.common.entity.SysConfig;
import com.zh.controlcenter.dao.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 *
 * @Author 
 * @Date 2023-12-22 10:18:18
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    SysConfigMapper sysConfigMapper;

    @Override
    public SysConfig getOne(Long id) {
        return sysConfigMapper.selectById(id);
    }

    @Override
    public List<SysConfig> list() {
        List<SysConfig> sysConfigs = sysConfigMapper.selectList(new QueryWrapper<SysConfig>().orderByDesc("create_time"));
        return sysConfigs;
    }

    @Override
    public Pager listByPage(Pager page) {
        IPage<SysConfig> pageInfo = sysConfigMapper.selectPage(new Page<>(page.getNum(), page.getSize()), new QueryWrapper<SysConfig>().orderByDesc("create_time"));
        return Pager.of(pageInfo);
    }

    @Override
    public boolean delete(Long id) {
        int i = sysConfigMapper.deleteById(id);
        return i > 0;
    }


}
