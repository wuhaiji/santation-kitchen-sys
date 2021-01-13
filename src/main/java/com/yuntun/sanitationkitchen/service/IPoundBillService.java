package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.bean.PoundBillBean;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.PoundBill;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PoundBillVo;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 地磅配置表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
public interface IPoundBillService extends IService<PoundBill> {

    SelectOptionVo selectPoundBillOption();

    RowData<PoundBillVo> findPoundBillList(PoundBillDto poundBillDto);

    /**
     * 分页列出磅单
     * @param poundBillDto
     * @return
     */
    RowData<PoundBill> pagePoundBill(PoundBillDto poundBillDto);

  /**
   * 单日磅单总量
   * @param poundBillDto
   */

   Integer getPoundDateTotal(PoundBillDto poundBillDto);


  void exportPoundBill(PoundBillDto poundBillDto, HttpServletResponse response);


}
