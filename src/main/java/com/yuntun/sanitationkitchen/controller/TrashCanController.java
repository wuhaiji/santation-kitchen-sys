package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.TrashCanDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.ITrashCanService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 垃圾桶表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/trashCan")
public class TrashCanController {

    @Autowired
    private ITrashCanService iTrashCanService;

    /**
     * 垃圾桶 下拉框
     *
     * @author wujihong
     * @since 2020-12-02 11:21
     */
    @Limit("trashCan:list")
    @RequestMapping("/option")
    public Result selectTrashCanOption() {
        return Result.ok(iTrashCanService.selectTrashCanOption());
    }

    /**
     * 分页查询垃圾桶
     * @author wujihong
     * @param trashCanDto
     * @since 2020-12-02 11:21
     */
    @Limit("trashCan:list")
    @RequestMapping("/list")
    public Result list(TrashCanDto trashCanDto) {
        ErrorUtil.PageParamError(trashCanDto.getPageSize(), trashCanDto.getPageNo());
        return Result.ok(iTrashCanService.findTrashCanList(trashCanDto));
    }

    /**
     * 根据uid查询垃圾桶
     * @author wujihong
     * @param uid
     * @since 2020-12-02 11:30
     */
    @Limit("trashCan:get")
    @RequestMapping("/get/{uid}")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iTrashCanService.findTrashCanByUid(uid));
    }

    /**
     * 插入垃圾桶
     * @author wujihong
     * @param trashCanDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/save")
    @Limit("trashCan:save")
    public Result save(@RequestBody TrashCanDto trashCanDto) {
        return Result.ok(iTrashCanService.insertTrashCan(trashCanDto));
    }

    /**
     * 根据uid修改垃圾桶
     * @author wujihong
     * @param trashCanDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/update")
    @Limit("trashCan:update")
    public Result update(@RequestBody TrashCanDto trashCanDto) {
        ErrorUtil.isObjectNull(trashCanDto.getUid(), "uid");
        return Result.ok(iTrashCanService.updateTrashCan(trashCanDto));
    }

    /**
     * 根据uid删除垃圾桶
     * @author wujihong
     * @param uids
     * @since 2020-12-02 12:12
     */
    @RequestMapping("/delete")
    @Limit("trashCan:delete")
    public Result delete(@RequestParam("uids[]") List<Long> uids) {
        ErrorUtil.isObjectNull(uids, "uids");
        return Result.ok(iTrashCanService.deleteTrashCan(uids));
    }

}
