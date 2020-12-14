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
    @Limit("facilitiesAndEquipment:trashCan:query")
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
    @Limit("facilitiesAndEquipment:trashCan:query")
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
    @Limit("facilitiesAndEquipment:trashCan:query")
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
    @Limit("facilitiesAndEquipment:trashCan:save")
    public Result save(@RequestBody TrashCanDto trashCanDto) {
        ErrorUtil.isObjectNullContent(trashCanDto, "垃圾桶信息");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getFacilityCode(), 2, 30, "设备编号");
        ErrorUtil.isObjectNull(trashCanDto.getFacilityType(), "设备类型");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getRfid(), 2, 30, "RFID");
        ErrorUtil.isObjectNull(trashCanDto.getRestaurantId(), "所属机构");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getAddress(), 2, 30, "地址");
        ErrorUtil.isObjectNull(trashCanDto.getCapacity(), "容量");
        ErrorUtil.verifyLatitudeAndLongitude(trashCanDto.getLongitude(), trashCanDto.getLatitude());
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getManufacturer(), 2, 30, "生产厂家");
        ErrorUtil.isObjectNull(trashCanDto.getManufacturer(), "联系人");
        ErrorUtil.notIllegalPhone(trashCanDto.getContactPersonPhone());
        return Result.ok(iTrashCanService.insertTrashCan(trashCanDto));
    }

    /**
     * 根据uid修改垃圾桶
     * @author wujihong
     * @param trashCanDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/update")
    @Limit("facilitiesAndEquipment:trashCan:update")
    public Result update(@RequestBody TrashCanDto trashCanDto) {
        ErrorUtil.isObjectNullContent(trashCanDto, "垃圾桶信息");
        ErrorUtil.isObjectNull(trashCanDto.getUid(), "uid");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getFacilityCode(), 2, 30, "设备编号");
        ErrorUtil.isObjectNull(trashCanDto.getFacilityType(), "设备类型");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getRfid(), 2, 30, "RFID");
        ErrorUtil.isObjectNull(trashCanDto.getRestaurantId(), "所属机构");
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getAddress(), 2, 30, "地址");
        ErrorUtil.isObjectNull(trashCanDto.getCapacity(), "容量");
        ErrorUtil.isObjectNull(trashCanDto.getCreateTime(), "创建时间");
        ErrorUtil.verifyLatitudeAndLongitude(trashCanDto.getLongitude(), trashCanDto.getLatitude());
        ErrorUtil.isStringLengthOutOfRange(trashCanDto.getManufacturer(), 2, 30, "生产厂家");
        ErrorUtil.isObjectNull(trashCanDto.getManufacturer(), "联系人");
        ErrorUtil.notIllegalPhone(trashCanDto.getContactPersonPhone());
        return Result.ok(iTrashCanService.updateTrashCan(trashCanDto));
    }

    /**
     * 根据uid删除垃圾桶
     * @author wujihong
     * @param uids
     * @since 2020-12-02 12:12
     */
    @RequestMapping("/delete")
    @Limit("facilitiesAndEquipment:trashCan:delete")
    public Result delete(@RequestParam(name = "uids[]", required = false) List<Long> uids) {
        ErrorUtil.isCollectionEmpty(uids,"uid");
        return Result.ok(iTrashCanService.deleteTrashCan(uids));
    }

}
