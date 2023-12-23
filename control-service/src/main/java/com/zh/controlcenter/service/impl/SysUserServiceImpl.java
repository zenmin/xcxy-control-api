package com.zh.controlcenter.service.impl;

import com.zh.controlcenter.common.apibase.Pager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.controlcenter.dao.mapper.SysUserMapper;
import com.zh.controlcenter.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.controlcenter.common.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系統用户
 *
 * @Author 
 * @Date 2023-04-27 11:40:51
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public SysUser getOne(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysUser> list() {
        List<SysUser> sysUsers = sysUserMapper.selectList(new QueryWrapper<SysUser>().orderByDesc("create_time"));
        return sysUsers;
    }

    @Override
    public Pager listByPage(Pager page) {
        IPage<SysUser> pageInfo = sysUserMapper.selectPage(new Page<>(page.getNum(), page.getSize()), new QueryWrapper<SysUser>().orderByDesc("create_time"));
        return Pager.of(pageInfo);
    }

    @Override
    public boolean delete(Long id) {
        int i = sysUserMapper.deleteById(id);
        return i > 0;
    }

}
