package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.util.RedisUtils;
import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.propertise.TrashDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.BitOperator;
import com.yuntun.sanitationkitchen.weight.util.CRC16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;


/**
 * 垃圾桶Modbus-rtu 解析
 *
 * @author wujihong
 */
@Component
public class ResolveModbusRTU implements ResolveProtocol {

    public static Logger logger = LoggerFactory.getLogger(ResolveModbusRTU.class);

    @Autowired
    private TrashDataPackageFormat trashDataPackageFormat;

    // 2.垃圾桶modbus-rtu 解析
    @Override
    public Boolean isResolve(byte[] dataBody) {
        int length = dataBody.length;
        Integer trashSize = trashDataPackageFormat.getTrashSize();

        if (length >= trashSize && dataBody[0] == 1) {
            // CRC校验
            byte[] nocrc = getNOCRC(dataBody);
            byte[] crcByteArray = CRC16.getCRCByteArray(nocrc);

            byte[] crc = getCRC(dataBody);
            return Arrays.equals(crcByteArray, crc);
        }
        return false;
    }

    @Override
    public byte[] getNewDataBody(byte[] dataBody) {
        Integer length = dataBody.length - trashDataPackageFormat.getTrashSize();
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
        Integer trashSize = trashDataPackageFormat.getTrashSize();

        if (length <= 0 && length < trashSize)
            return skDataBody;

        int count = length / trashSize;
        int i = 0;
        while (i < count) {
            // 判断是否能被解析
            if (isResolve(dataBody)) {
                Double trashWeight = getTrashWeight(dataBody);
                logger.info("获取垃圾桶weight：{}", trashWeight);
                skDataBody.setTrashWeight(trashWeight);
                break;
            }
            dataBody = getNewDataBody(dataBody);
            i++;
        }

        return skDataBody;
    }

    public Double getTrashWeight(byte[] dataBody) {
        Integer weightDataIndex = trashDataPackageFormat.getDataIndex();
        Integer weightDataSize = trashDataPackageFormat.getDataSize();
        byte[] weightBytes = new byte[weightDataSize];
        System.arraycopy(dataBody, weightDataIndex, weightBytes, 0, weightDataSize);

        Double weight = (double) (BitOperator.byteToInteger(weightBytes) / 100);
        System.out.println("垃圾桶重量："+weight);

        return weight;
    }

    public byte[] getCRC(byte[] dataBody) {
        Integer crcIndex = trashDataPackageFormat.getCRCIndex();
        Integer crcSize = trashDataPackageFormat.getCRCSize();

        byte[] CRC = new byte[crcSize];
        System.arraycopy(dataBody,crcIndex,CRC,0,crcSize);

        return CRC;
    }

    public byte[] getNOCRC(byte[] dataBody) {
        Integer NOCRCSize = trashDataPackageFormat.getTrashSize() - trashDataPackageFormat.getCRCSize();

        byte[] NOCRC = new byte[NOCRCSize];
        System.arraycopy(dataBody,0, NOCRC, 0, NOCRCSize);
        return NOCRC;
    }
}
