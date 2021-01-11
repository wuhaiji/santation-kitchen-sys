package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.propertise.WeightDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 地磅解析
 *
 * @author wujihong
 */
@Component
public class ResolveWeight implements ResolveProtocol {

    public static Logger logger = LoggerFactory.getLogger(ResolveModbusRTU.class);

    @Autowired
    private WeightDataPackageFormat weightDataPackageFormat;

    // 3.地磅解析
    public Boolean isResolve(byte[] dataBody) {
        int length = dataBody.length;
        Integer size = weightDataPackageFormat.getSize();
        byte beginFlag = weightDataPackageFormat.getBeginFlag();
        byte endFlag = weightDataPackageFormat.getEndFlag();
        if (length >= size && dataBody[0] == beginFlag && dataBody[size-1] == endFlag) {
            // 异或校验
            byte[] noVerify = getNoVerify(dataBody);
            byte[] verifyByteArray = XOR.getVerify(noVerify);

            byte[] verify = getVerifyByteArray(dataBody);
            for (byte b:verifyByteArray) {
                System.out.println(b);
            }
            return Arrays.equals(verifyByteArray, verify);
        }
        return false;
    }

    @Override
    public byte[] getNewDataBody(byte[] dataBody) {
        Integer length = dataBody.length - weightDataPackageFormat.getSize();
        byte[] newDataBody = new byte[length];
        System.arraycopy(dataBody,0, newDataBody,0,length);
        return newDataBody;
    }

    @Override
    public SKDataBody resolve(byte[] dataBody) {
        return null;
    }

    @Override
    public SKDataBody resolveAll(byte[] dataBody) {
        SKDataBody skDataBody = new SKDataBody();
        int length = dataBody.length;
        Integer boundWeightSize = weightDataPackageFormat.getSize();

        if (length < boundWeightSize)
            return skDataBody;

        int count = length / boundWeightSize;
        int i = 0;
        while (i < count) {
            // 判断是否能被解析
            if (isResolve(dataBody)) {
                Double boundWeight = getBoundWeight(dataBody);
                logger.info("获取地磅车辆weight：{}", boundWeight);
                skDataBody.setBoundWeight(boundWeight);
                break;
            }
            dataBody = getNewDataBody(dataBody);
            i++;
        }

        return skDataBody;
    }

    public Double getBoundWeight(byte[] dataBody) {
        Integer size = weightDataPackageFormat.getData().getSize();
        Integer symbolIndex = weightDataPackageFormat.getData().getSymbolIndex();
        Integer symbolSize = weightDataPackageFormat.getData().getSymbolSize();
        byte[] symbolBytes = new byte[symbolSize];
        System.arraycopy(dataBody,symbolIndex,symbolBytes,0,symbolSize);
        String symbol = BitOperator.byteArrayToHex(symbolBytes);
        if (symbol.equals("2B")) {
            symbol = "+";
        } else {
            symbol = "-";
        }

        Integer integerIndex = weightDataPackageFormat.getData().getIntegerIndex();
        Integer integerSize = weightDataPackageFormat.getData().getIntegerSize();
        byte[] integerBytes = new byte[integerSize];
        System.arraycopy(dataBody,integerIndex,integerBytes,0,integerSize);
        String integer = "0";
        try {
            integer = HexUtils.getTenBytes(integerBytes).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer decimalIndex = weightDataPackageFormat.getData().getDecimalIndex();
        Integer decimalSize = weightDataPackageFormat.getData().getDecimalSize();
        byte[] decimalBytes = new byte[decimalSize];
        System.arraycopy(dataBody,decimalIndex,decimalBytes,0,decimalSize);
        String decimal = "0";
        try {
            if (decimalBytes[0] > 32) {
                decimal = HexUtils.getTenBytes(decimalBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = symbol+integer+"."+decimal;
        System.out.println("result:"+result);
        return Double.valueOf(result);
    }

    // 去除了校验码
    public byte[] getNoVerify(byte[] dataBody) {
        Integer noVerifySize = weightDataPackageFormat.getSize() - weightDataPackageFormat.getVerifySize();

        byte[] noVerify = new byte[noVerifySize];
        System.arraycopy(dataBody,0, noVerify,0, noVerifySize-1);
        // 结尾标识
        noVerify[noVerifySize-1] = weightDataPackageFormat.getEndFlag();
        return noVerify;
    }

    public byte[] getVerifyByteArray(byte[] dataBody) {
        Integer verifyIndex = weightDataPackageFormat.getVerifyIndex();
        Integer verifySize = weightDataPackageFormat.getVerifySize();

        byte[] verify = new byte[verifySize];
        System.arraycopy(dataBody, verifyIndex, verify,0, verifySize);
        return verify;
    }
}
