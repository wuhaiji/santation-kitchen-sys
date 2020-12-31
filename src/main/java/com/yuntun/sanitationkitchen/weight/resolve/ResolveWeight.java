//package com.yuntun.sanitationkitchen.weight.resolve;
//
//import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
//import com.yuntun.sanitationkitchen.weight.propertise.WeightDataPackageFormat;
//import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 地磅解析
// *
// * @author wujihong
// */
//@Component
//public class ResolveWeight implements ResolveProtocol {
//
//    @Autowired
//    private WeightDataPackageFormat weightDataPackageFormat;
//
////    public static WeightDataPackageFormat weightDataPackageFormat = SpringUtil.getBean(WeightDataPackageFormat.class);
//
//    // 3.地磅解析
//    public Boolean isResolve(byte[] dataBody) {
//        return false;
//    }
//
//    @Override
//    public byte[] getNewDataBody(byte[] dataBody) {
//        return new byte[0];
//    }
//
//    @Override
//    public SKDataBody resolve(byte[] dataBody) {
//        return null;
//    }
//
//    @Override
//    public SKDataBody resolveAll(byte[] dataBody) {
//        return null;
//    }
//}
