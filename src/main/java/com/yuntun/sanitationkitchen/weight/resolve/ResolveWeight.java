package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.propertise.WeightDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import org.springframework.stereotype.Component;

/**
 * 地磅解析
 *
 * @author wujihong
 */
@Component
public class ResolveWeight implements ResolveProtocol {

    public static WeightDataPackageFormat weightDataPackageFormat = SpringUtil.getBean(WeightDataPackageFormat.class);

    // 3.地磅解析
    public void resolve(byte[] data) {

    }
}
