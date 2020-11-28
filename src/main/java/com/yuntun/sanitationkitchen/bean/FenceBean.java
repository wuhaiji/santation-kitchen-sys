package com.yuntun.sanitationkitchen.bean;

import com.venton.ss.common.core.NetAreaPoint;
import java.util.List;
import lombok.Data;

@Data
public class FenceBean {

    private Long id;

    private Long fenceId;

    private String fenceName;

    private Long districtId;

    private String districtName;

    private String area;

    private Long chargerId;

    private String chargerName;

    private String chargerPhone;

    private List<Long> vehicleIds;

    private List<NetAreaPoint> coordinates;
    /**
     * 封装车辆类型及对应的数量
     */
    private List<FenceVehicleDetails> fenceVehicleDetailsList;
}
