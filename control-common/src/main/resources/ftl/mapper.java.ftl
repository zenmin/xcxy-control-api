package com.zh.controlcenter.dao.mapper;

import com.zh.controlcenter.common.entity.${entity};
import ${superMapperClassPackage};

/**
 * ${table.comment!}
 *
 * @Author ${author}
 * @Date ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
