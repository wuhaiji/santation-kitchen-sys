package com.yuntun.sanitationkitchen.mapper;

import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.SanitationOfficeValue;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
* <p>
    * 后台管理系统用户表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-12-01
*/
@Mapper
public interface SanitationOfficeMapper extends BaseMapper<SanitationOffice> {

    // 查询所有的uid和name
    List<SanitationOfficeValue> selectSanitationOfficeOption();

}
