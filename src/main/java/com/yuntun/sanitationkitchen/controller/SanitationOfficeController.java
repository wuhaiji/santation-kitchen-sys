package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
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

    @RequestMapping("/option")
    @Limit("sanitationOffice:option")
    public Result<Object> option() {
        List<SanitationOffice> list = iSanitationOfficeService.list();
        List<OptionsVo> collect = list.parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getName()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(collect);
    }

    /**
     * 分页查询后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:21
     */
    @Limit("sanitationOffice:list")
    @RequestMapping("/list")
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
    @Limit("sanitationOffice:get")
    @RequestMapping("/get/{uid}")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iSanitationOfficeService.findSanitationOfficeServiceByUid(uid));
    }

    /**
     * 插入后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/save")
    @Limit("sanitationOffice:save")
    public Result save(@RequestBody SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNullContent(sanitationOfficeDto, "单位信息");
        return Result.ok(iSanitationOfficeService.insertSanitationOffice(sanitationOfficeDto));
    }

    /**
     * 根据uid修改后台管理系统用户表
     *
     * @param sanitationOfficeDto
     * @author wujihong
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/update")
    @Limit("sanitationOffice:update")
    public Result update(@RequestBody SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNull(sanitationOfficeDto.getUid(), "uid");
        return Result.ok(iSanitationOfficeService.updateSanitationOffice(sanitationOfficeDto));
    }

    /**
     * 根据uid删除后台管理系统用户表
     *
     * @param uids
     * @author wujihong
     * @since 2020-12-02 12:12
     */
    @RequestMapping("/delete")
    @Limit("sanitationOffice:delete")
    public Result delete(@RequestParam(name = "uids[]", required = false) List<Long> uids) {
        ErrorUtil.isListEmpty(uids,"uid");
        return Result.ok(iSanitationOfficeService.deleteSanitationOffice(uids));
    }
}
