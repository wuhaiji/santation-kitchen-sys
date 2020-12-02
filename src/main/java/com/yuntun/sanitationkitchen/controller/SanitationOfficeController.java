package com.yuntun.sanitationkitchen.controller;


import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IFuelDeviceService;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class SanitationOfficeController{

    @Autowired
    private ISanitationOfficeService iSanitationOfficeService;

    /**
     * 分页查询后台管理系统用户表
     * @author wujihong
     * @param sanitationOfficeDto
     * @since 2020-12-02 11:21
     */
    @Limit("SanitationOffice:list")
    @GetMapping("/list")
    public Result list(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.PageParamError(sanitationOfficeDto.getPageSize(), sanitationOfficeDto.getPageNo());
        return Result.ok(iSanitationOfficeService.findSanitationOfficeServiceList(sanitationOfficeDto));
    }

    /**
     * 根据uid查询后台管理系统用户表
     * @author wujihong
     * @param uid
     * @since 2020-12-02 11:30
     */
    @Limit("SanitationOffice:get")
    @GetMapping("/get/{uid}")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iSanitationOfficeService.findSanitationOfficeServiceByUid(uid));
    }

    /**
     * 插入后台管理系统用户表
     * @author wujihong
     * @param sanitationOfficeDto
     * @since 2020-12-02 11:39
     */
    @PostMapping("/save")
    @Limit("SanitationOffice:save")
    public Result save(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNull(sanitationOfficeDto.getName(), "用户名不能为空");
        ErrorUtil.isObjectNull(sanitationOfficeDto.getManagerId(), "管理员id不能为空");
        return Result.ok(iSanitationOfficeService.insertSanitationOffice(sanitationOfficeDto));
    }

    /**
     * 根据uid修改后台管理系统用户表
     * @author wujihong
     * @param sanitationOfficeDto
     * @since 2020-12-02 11:39
     */
    @PostMapping("/update")
    @Limit("SanitationOffice:update")
    public Result update(SanitationOfficeDto sanitationOfficeDto) {
        ErrorUtil.isObjectNull(sanitationOfficeDto.getUid(), "uid不能为空");
        return Result.ok(iSanitationOfficeService.updateSanitationOffice(sanitationOfficeDto));
    }

    /**
     * 根据uid删除后台管理系统用户表
     * @author wujihong
     * @param uid
     * @since 2020-12-02 12:12
     */
    @PostMapping("/delete/{uid}")
    @Limit("SanitationOffice:delete")
    public Result delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "uid");
        return Result.ok(iSanitationOfficeService.deleteSanitationOffice(uid));
    }

}
