package com.zh.controlcenter.service;

import com.zh.controlcenter.common.apibase.Pager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.controlcenter.common.entity.SysConfig;
import java.util.List;

/**
 * 
 *
 * @Author 
 * @Date 2023-12-22 10:18:18
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    SysConfig getOne(Long id);

    /**
     * 不分页查询
     *
     * @return
     */
    List<SysConfig> list();

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

    void addPersonCount(int i);
}
