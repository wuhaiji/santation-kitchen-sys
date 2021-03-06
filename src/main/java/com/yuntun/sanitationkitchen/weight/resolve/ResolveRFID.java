package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.propertise.RFIDDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.BitOperator;
import com.yuntun.sanitationkitchen.weight.util.RFIDCRC16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * rfid读卡器 解析
 *
 * @author wujihong
 */
@Component
public class ResolveRFID implements ResolveProtocol {

    public static Logger logger = LoggerFactory.getLogger(ResolveRFID.class);

    @Autowired
    private RFIDDataPackageFormat rfidDataPackageFormat;

    // 1.rfid读卡器 解析
    public Boolean isResolve(byte[] dataBody) {
        int length = dataBody.length;
        int rfidSize = rfidDataPackageFormat.getRfidSize();

        // rfid的第一位字节为数据包的长度（但不包括len本身）
        int len = dataBody[0] & 0xFF;
        if (length >= rfidSize && len == rfidSize-1) {
            // CRC校验
            byte[] nocrcrfid = getNOCRCRFID(dataBody);
            byte[] crcByteArray = RFIDCRC16.getCRCByteArray(nocrcrfid);

            byte[] crc = getCRC(dataBody);
            return Arrays.equals(crcByteArray, crc);
        }
        return false;
    }

    @Override
    public byte[] getNewDataBody(byte[] dataBody) {
        int length = dataBody.length - rfidDataPackageFormat.getRfidSize();
        byte[] newDataBody = new byte[length];
        System.arraycopy(dataBody,0, newDataBody,0,length);
        return newDataBody;
    }

    @Override
    public SKDataBody resolve(byte[] dataBody) {
        SKDataBody skDataBody = new SKDataBody();
        Set<String> epcs = skDataBody.getEpcs();

        String epc = getEPC(dataBody);
        if (epcs != null) {
            epcs.add(epc);
        }
        else {
            Set<String> epcss = new HashSet<>();
            epcss.add(epc);
            skDataBody.setEpcs(epcss);
        }

        return skDataBody;
    }

    @Override
    public SKDataBody resolveAll(byte[] dataBody) {
        SKDataBody skDataBody = new SKDataBody();
        Set<String> epcs = new HashSet<>();
        int length = dataBody.length;
        int rfidSize = rfidDataPackageFormat.getRfidSize();

        if (length <= 0 && length < rfidSize)
            return skDataBody;

        int count = length / rfidSize;
        int i = 0;
        while (i < count) {
            // 判断是否能被解析
            if (isResolve(dataBody)) {
                String epc = getEPC(dataBody);
                logger.info("epc：{}", epc);
                epcs.add(epc);
            }
            dataBody = getNewDataBody(dataBody);
            i++;
        }

        if (epcs.size() > 0) {
            skDataBody.setEpcs(epcs);
            logger.info("解析RFID数据：{}", skDataBody);
        }

        return skDataBody;
    }

    /**
     * 获取数据体的rfid:18字节
     *
     * @param dataBody
     * @return
     */
    public byte[] getRFID(byte[] dataBody) {
        Integer RFIDSize = rfidDataPackageFormat.getRfidSize();

        byte[] RFID = new byte[RFIDSize];
        System.arraycopy(dataBody, 0, RFID,0, RFIDSize);

        return RFID;
    }

    public byte[] getNOCRCRFID(byte[] dataBody) {
        Integer NOCRCRFIDSize = rfidDataPackageFormat.getRfidSize()-rfidDataPackageFormat.getCRCSize();

        byte[] NOCRCRFID = new byte[NOCRCRFIDSize];
        System.arraycopy(dataBody, 0, NOCRCRFID,0, NOCRCRFIDSize);

        return NOCRCRFID;
    }

    public byte[] getCRC(byte[] dataBody) {
        byte[] rfid = getRFID(dataBody);
        int rfidSize = rfidDataPackageFormat.getRfidSize();
        int crcIndex = rfidDataPackageFormat.getCRCIndex();
       return Arrays.copyOfRange(rfid, crcIndex, rfidSize);
    }

    /**
     * 获取数据体的rfid的epc号：12字节
     *
     * @param RFID
     * @return
     */
    public String getEPC(byte[] RFID) {
        // 获取rfid
        byte[] rfid = getRFID(RFID);
        Integer epcIndex = rfidDataPackageFormat.getEpcIndex();
        Integer epcSize = rfidDataPackageFormat.getEpcSize();

        // 获取rfid的epc号
        byte[] EPC = new byte[epcSize];
        System.arraycopy(rfid, epcIndex, EPC,0, epcSize);

        return BitOperator.byteArrayToHex(EPC).trim();
    }

}
