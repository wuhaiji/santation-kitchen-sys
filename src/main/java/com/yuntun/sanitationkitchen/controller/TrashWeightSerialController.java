package com.yuntun.sanitationkitchen.controller;


import com.yuntun.sanitationkitchen.model.dto.BasePageDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.ITrashWeightSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 垃圾桶称重流水
 * @author tang
 * @since 2020/12/15
 */
@RestController("/trash/weight")
public class TrashWeightSerialController {

    @Autowired
    private ITrashWeightSerialService trashWeightSerialService;


    @RequestMapping("/page")
    public Result page(){

        return null;
    }


}
