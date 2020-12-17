package com.yuntun.sanitationkitchen.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.RestaurantCode;
import com.yuntun.sanitationkitchen.model.dto.TrashCanDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Cron;
import com.yuntun.sanitationkitchen.model.entity.Restaurant;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.model.vo.TrashCanVo;
import com.yuntun.sanitationkitchen.service.ICronService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 定时任务表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-15
 */
@RestController
@RequestMapping("/cron")
@Slf4j
public class CronController {

    @Autowired
    ICronService iCronService;

    @Limit("cron:list")
    @GetMapping("/list")
    public Result<Object> list(VehicleListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Cron> iPage = iCronService.page(
                new Page<Cron>().setSize(dto.getPageSize()).setCurrent(dto.getPageNo()),
                new QueryWrapper<Cron>().orderByDesc("create_time")
        );
        List<CronListVo> vos = ListUtil.listMap(CronListVo.class, iPage.getRecords());
        RowData<CronListVo> data = new RowData<CronListVo>().setRows(vos).setTotal(iPage.getTotal()).setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }


    @GetMapping("/get/{uid}")
    @Limit("cron:query")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");
        Cron byId = iCronService.getOne(new QueryWrapper<Cron>().eq("uid", uid));
        if (EptUtil.isEmpty(byId)) {
            log.error("Cron->save->查询定时任务失败,uid:{}",uid);
            return Result.error(RestaurantCode.ID_NOT_EXIST);
        }
        CronGetVo vo = new CronGetVo();
        BeanUtils.copyProperties(byId, vo);

        return Result.ok(vo);
    }

    @PostMapping("/save")
    @Limit("cron:save")
    public Result<Object> save(CronSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getCronKey(), "执行类不能为空");
        ErrorUtil.isObjectNull(dto.getCronExpression(), "cron表达式不能为空");


        Cron cron = new Cron();
        BeanUtils.copyProperties(dto, cron);
        boolean save = iCronService.save(cron);
        if (!save) {
            log.error("Cron->save->保存定时任务失败,dto:{}", JSON.toJSONString(dto));
            return Result.error(RestaurantCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("cron:update")
    public Result<Object> update(CronUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getId(), "id");

        Cron entity = new Cron();
        BeanUtils.copyProperties(dto, entity);
        boolean save = iCronService.updateById(entity);
        if (!save) {
            log.error("Cron->update->修改定时任务失败,dto:{}", JSON.toJSONString(dto));
            return Result.error(RestaurantCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/{uid}")
    @Limit("cron:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "uid");

        boolean b = iCronService.removeById(uid);

        if (!b) {
            log.error("Restaurant->delete->删除餐馆失败,uid:{}", uid);
            return Result.error(RestaurantCode.DELETE_ERROR);
        }
        return Result.ok();

    }



    @Data
    @Accessors(chain = true)
    public static class CronListDto {
        Integer pageSize;
        Integer pageNo;
        /**
         * 主键id
         */
        private Integer id;
        /**
         * 定时任务完整类名
         */
        private String cronKey;
        /**
         * cron表达式
         */
        private String cronExpression;
        /**
         * 任务描述
         */
        private String taskExplain;
        /**
         * 状态,1:正常;2:停用
         */
        private Integer status;

    }

    @Data
    @Accessors(chain = true)
    public static class CronListVo {
        Integer pageSize;
        Integer pageNo;
        /**
         * 主键id
         */
        private Integer id;
        /**
         * 定时任务完整类名
         */
        private String cronKey;
        /**
         * cron表达式
         */
        private String cronExpression;
        /**
         * 任务描述
         */
        private String taskExplain;
        /**
         * 状态,1:正常;2:停用
         */
        private Integer status;

    }

    @Data
    @Accessors(chain = true)
    public static class CronGetVo {

        /**
         * 主键id
         */
        private Integer id;
        /**
         * 定时任务完整类名
         */
        private String cronKey;
        /**
         * cron表达式
         */
        private String cronExpression;
        /**
         * 任务描述
         */
        private String taskExplain;
        /**
         * 状态,1:正常;2:停用
         */
        private Integer status;

    }

    @Data
    @Accessors(chain = true)
    public static class CronSaveDto {

        /**
         * 定时任务完整类名
         */
        private String cronKey;
        /**
         * cron表达式
         */
        private String cronExpression;
        /**
         * 任务描述
         */
        private String taskExplain;
        /**
         * 状态,1:正常;2:停用
         */
        private Integer status;

    }

    @Data
    @Accessors(chain = true)
    public static class CronUpdateDto {
        /**
         * 主键id
         */
        private Integer id;
        /**
         * 定时任务完整类名
         */
        private String cronKey;
        /**
         * cron表达式
         */
        private String cronExpression;
        /**
         * 任务描述
         */
        private String taskExplain;
        /**
         * 状态,1:正常;2:停用
         */
        private Integer status;

    }

}

