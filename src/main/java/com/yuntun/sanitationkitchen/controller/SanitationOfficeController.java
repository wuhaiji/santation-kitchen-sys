package com.yuntun.sanitationkitchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理系统用户表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/sanitationOffice")
public class SanitationOfficeController {

    @Autowired
    private ISanitationOfficeService iSanitationOfficeService;

    /**
     * 分页查询后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:21
     */

    @GetMapping("/list")
    @Limit("system:sanitationOffice:query")
    public Result list(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.PageParamError(sanitationOfficeDto.getPageSize(), sanitationOfficeDto.getPageNo());
        return Result.ok(iSanitationOfficeService.findSanitationOfficeServiceList(sanitationOfficeDto));
    }

    /**
     * 根据uid查询后台管理系统用户表
     *
     * @param uid
     * @author wujihong
     * @since 2020-12-02 11:30
     */

    @GetMapping("/get/{uid}")
    @Limit("system:sanitationOffice:query")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iSanitationOfficeService.findSanitationOfficeServiceByUid(uid));
    }

    @GetMapping("/options")
    @Limit("system:sanitationOffice:query")
    public Result<Object> options() {
        List<SanitationOffice> list = iSanitationOfficeService.list();
        List<OptionsVo> collect = list.parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getName()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(collect);
    }

    /**
     * 插入后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:39
     */
    @PostMapping("/save")
    @Limit("system:sanitationOffice:save")
    public Result save(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNull(sanitationOfficeDto.getName(), "用户名");
        ErrorUtil.isObjectNull(sanitationOfficeDto.getManagerId(), "管理员id");
        return Result.ok(iSanitationOfficeService.insertSanitationOffice(sanitationOfficeDto));
    }



    /**
     * 根据uid修改后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:39
     */
    @PostMapping("/update")
    @Limit("system:sanitationOffice:update")
    public Result update(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNull(sanitationOfficeDto.getUid(), "uid");
        return Result.ok(iSanitationOfficeService.updateSanitationOffice(sanitationOfficeDto));
    }

    /**
     * 根据uid删除后台管理系统用户表
     *
     * @param uid
     * @author wujihong
     * @since 2020-12-02 12:12
     */
    @PostMapping("/delete/{uid}")
    @Limit("system:sanitationOffice:delete")
    public Result delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "uid");
        return Result.ok(iSanitationOfficeService.deleteSanitationOffice(uid));
    }

    @PostMapping("/delete/batch")
    @Limit("system:sanitationOffice:delete")
    public Result<Object> delete(@RequestParam("ids") List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "id");
        boolean b = iSanitationOfficeService.remove(new QueryWrapper<SanitationOffice>().in("uid", ids));
        if (b)
            return Result.ok();
        return Result.error(UserCode.DELETE_SYSUSER_FAILURE);

    }
}
