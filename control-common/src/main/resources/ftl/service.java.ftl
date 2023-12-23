package ${package.Service};

import com.zh.controlcenter.common.apibase.Pager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.controlcenter.common.entity.${entity};
import java.util.List;

/**
 * ${table.comment!}
 *
 * @Author ${author}
 * @Date ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
public interface ${table.serviceName} extends IService<${entity}> {

    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    ${entity} getOne(Long id);

    /**
     * 不分页查询
     *
     * @return
     */
    List<${entity}> list();

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
