package com.zh.controlcenter.service;

import com.zh.controlcenter.common.apibase.Pager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.controlcenter.common.entity.SysUser;

import java.util.List;

/**
 * 系統用户
 *
 * @Author 
 * @Date 2023-04-27 11:40:51
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    SysUser getOne(Long id);

    /**
     * 不分页查询
     *
     * @return
     */
    List<SysUser> list();

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    Pager listByPage(Pager page);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

}
