package ${package.Controller};

<#if swagger2>
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>
import org.springframework.web.bind.annotation.*;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.zh.controlcenter.common.entity.${entity};
import ${package.Service}.${entity}Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.zh.controlcenter.common.apibase.ResponseEntity;
import com.zh.controlcenter.common.apibase.Pager;

/**
 * ${table.comment}
 *
 * @Author ${author}
 * @Date ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
<#if swagger2>
@Tag(name = "${table.comment}")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/api/${entity?substring(0, 1)?lower_case +  entity?substring(1)}")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    ${entity}Service ${entity?substring(0, 1)?lower_case +  entity?substring(1)}Service;

    /**
     * 根据id查询一条数据
     *
     * @param id
     * @return
     */
    <#if swagger2>
    @Operation(summary = "查询一条数据")
    </#if>
    @GetMapping("/getOne/{id}")
    public ResponseEntity<${entity}> getOne(<#if swagger2>@Parameter(name = "id", description = "主键", required = true) </#if>@PathVariable Long id) {
        return ResponseEntity.success(${entity?substring(0, 1)?lower_case +  entity?substring(1)}Service.getOne(id));
    }

    /**
     * 查询全部（分页）
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    <#if swagger2>
    @Operation(summary = "查询全部（分页）")
    </#if>
    @Parameters({@Parameter(description = "页码", name = "pageNum", example = "1"), @Parameter(description = "分页大小", name = "pageSize", example = "10")})
    @GetMapping("/getListByPage/{pageNum}/{pageSize}")
    public ResponseEntity<Pager<${entity}>> listByPage(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return ResponseEntity.success(${entity?substring(0, 1)?lower_case +  entity?substring(1)}Service.listByPage(new Pager(pageNum, pageSize)));
    }

    /**
     * 带ID更新 不带ID新增
     *
     * @param ${entity?substring(0, 1)?lower_case +  entity?substring(1)}
     * @return
     */
    <#if swagger2>
    @Operation(summary = "新增/更新", description = "带id更新 不带id新增")
    </#if>
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveOrUpdate(@RequestBody ${entity} ${entity?substring(0, 1)?lower_case +  entity?substring(1)}) {
        return ResponseEntity.success(${entity?substring(0, 1)?lower_case +  entity?substring(1)}Service.saveOrUpdate(${entity?substring(0, 1)?lower_case +  entity?substring(1)}));
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    <#if swagger2>
    @Operation(summary = "删除数据")
    </#if>
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(<#if swagger2>@Parameter(name = "id", description = "主键", required = true) </#if>@PathVariable Long id) {
        return ResponseEntity.success(${entity?substring(0, 1)?lower_case +  entity?substring(1)}Service.delete(id));
    }


}