//package com.yuntun.sanitationkitchen.weight.resolve;
//
//import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
//import com.yuntun.sanitationkitchen.weight.propertise.TrashDataPackageFormat;
//import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 垃圾桶Modbus-rtu 解析
// *
// * @author wujihong
// */
//@Component
//public class ResolveModbusRTU implements ResolveProtocol {
//
//    @Autowired
//    private TrashDataPackageFormat trashDataPackageFormat;
//
////    public static TrashDataPackageFormat trashDataPackageFormat = SpringUtil.getBean(TrashDataPackageFormat.class);
//
//    // 2.垃圾桶modbus-rtu 解析
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
