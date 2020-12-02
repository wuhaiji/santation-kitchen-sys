package com.yuntun.sanitationkitchen.vehicle.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuntun.sanitationkitchen.bean.AsdsBean;
import com.yuntun.sanitationkitchen.bean.MileBean;
import com.yuntun.sanitationkitchen.bean.TireBean;
import com.yuntun.sanitationkitchen.bean.TrackBean;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import com.yuntun.sanitationkitchen.config.ApiStatusConfig;
import com.yuntun.sanitationkitchen.config.ThirdApiConfig;
import com.yuntun.sanitationkitchen.util.HttpUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

/**
 * @author yookfeng 2020/8/17
 * @des 车辆API
 **/
@Component
public class VehicleApi implements IVehicle {

  /**
   * 查询车辆信息
   *
   * @return
   */
  @Override
  public List<VehicleBean> getVehicleInfo() {
    List<VehicleBean> vehicleBeanList = new ArrayList<>();
    VehicleBean vehicleBean;
    Map<String, Object> params = new HashMap<>(2);
    params.put("key", ThirdApiConfig.key);
    params.put("type", "1");
    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/deviceData/vehicleBaseInfo.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONArray jsonArray = jsonObject.getJSONArray("obj");
      for (int i = 0; i < jsonArray.size(); i++) {
        vehicleBean = new VehicleBean();
        JSONObject jb = jsonArray.getJSONObject(i);
        vehicleBean.setTerminalNo(jb.getString("terminalNo"));
        vehicleBean.setSim(jb.getString("sim"));
        vehicleBean.setVehicleDeviceId(jb.getString("id"));
        vehicleBean.setTerminalNo(jb.getString("terminalType"));
        vehicleBean.setPlateNo(jb.getString("plate"));
        vehicleBeanList.add(vehicleBean);
      }
    }
    return vehicleBeanList;
  }


  /**
   * 获取车辆s实时信息
   *
   * @param ids
   * @return
   */
  @Override
  public List<VehicleBean> getVehicleRealtimeData(String ids) {
    List<VehicleBean> vehicleBeanList = new ArrayList<>();
    VehicleBean vehicleBean;
    Map<String, Object> params = new HashMap<>(3);
    params.put("key", ThirdApiConfig.key);
    params.put("ids", ids);
    params.put("type", "1");
    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/deviceData/realtimeData.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONArray jsonArray = jsonObject.getJSONArray("obj");
      for (int i = 0; i < jsonArray.size(); i++) {
        vehicleBean = new VehicleBean();
        JSONObject jb = jsonArray.getJSONObject(i);
        vehicleBean.setTerminalNo(jb.getString("terminalNo"));
        vehicleBean.setSim(jb.getString("sim"));
        vehicleBean.setVehicleDeviceId(jb.getString("id"));
        vehicleBean.setTerminalNo(jb.getString("terminalType"));
        vehicleBeanList.add(vehicleBean);
      }
    }
    return vehicleBeanList;
  }

  /**
   * 查询用户下所有车实时当前状态数据
   *
   * @return
   */
  @Override
  public List<VehicleBean> getAllRealtimeData() {
    List<VehicleBean> vehicleBeanList = new ArrayList<>();
    List<TireBean> tireBeanList = new ArrayList<>();
    VehicleBean vehicleBean;
    Map<String, Object> params = new HashMap<>(3);
    params.put("key", ThirdApiConfig.key);
    params.put("type", "1");
    params.put("filterTime", String.valueOf(System.currentTimeMillis()));

    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/deviceData/allRealData.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONObject obj = jsonObject.getJSONObject("obj");
      JSONArray jsonArray = obj.getJSONArray("data");
      for (int i = 0; i < jsonArray.size(); i++) {
        vehicleBean = new VehicleBean();
        JSONObject jb = jsonArray.getJSONObject(i);
        vehicleBean.setTerminalNo(jb.getString("terminalNo"));
        vehicleBean.setSim(jb.getString("sim"));
        vehicleBean.setVehicleDeviceId(jb.getString("id"));
        vehicleBean.setTerminalNo(jb.getString("terminalType"));
        //车辆平台状态
        vehicleBean.setVehicleDeviceStatus(Integer.parseInt(jb.getString("vehicleStatus")));
        //胎压信息
        JSONArray tireArray = jb.getJSONArray("tirePressureInfo");
        for (int j = 0; j < tireArray.size(); j++) {
          JSONObject tireObject = tireArray.getJSONObject(j);
          TireBean tireBean = new TireBean();
          tireBean.setAlarmStatus(tireObject.getString("alarmStatus"));
          tireBean.setTyrePemperature(tireObject.getString("tyreIdentifier"));
          tireBean.setTyrePressure(tireObject.getString("tyrePressure"));
          tireBean.setTyrePressure(tireObject.getString("tyrePemperature"));
          tireBean.setStatus(tireObject.getString("status"));
          tireBeanList.add(tireBean);
          vehicleBean.setTireBeans(tireBeanList);
        }

        vehicleBeanList.add(vehicleBean);
      }
    }
    return vehicleBeanList;
  }

  /**
   * 查询单个车辆里程数据
   *
   * @param id
   * @param startTime
   * @param endTime
   * @return
   */
  @Override
  public List<MileBean> getMileageData(String id, Long startTime, Long endTime) {
    List<MileBean> mileBeanList = new ArrayList<>();
    Map<String, Object> params = new HashMap<>(5);
    params.put("key", ThirdApiConfig.key);
    params.put("type", "1");
    params.put("id", id);
    params.put("startTime", String.valueOf(System.currentTimeMillis()));
    params.put("endTime", String.valueOf(System.currentTimeMillis()));
    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/mileage/mileageData.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      String flag = jsonObject.getString("flag");
      if (ApiStatusConfig.ok.equals(flag)) {
        JSONArray jsonArray = jsonObject.getJSONArray("obj");
        for (int i = 0; i < jsonArray.size(); i++) {
          MileBean mileBean = new MileBean();
          JSONObject jb = jsonArray.getJSONObject(i);
          mileBean.setPlate(jb.getString("plate"));
          mileBean.setBeginTime(jb.getString("beginTime"));
          mileBean.setEndTime(jb.getString("endTime"));
          mileBean.setLatBegin(jb.getString("latBegin"));
          mileBean.setLatEnd(jb.getString("latEnd"));
          mileBean.setLonBegin(jb.getString("lonBegin"));
          mileBean.setLonEnd(jb.getString("lonEnd"));
          mileBean.setMileageBegin(jb.getString("mileageBegin"));
          mileBean.setMileageEnd(jb.getString("mileageEnd"));
          mileBean.setSpeed(jb.getString("speed"));
          mileBean.setThisMileage(jb.getString("thisMileage"));
          mileBean.setThisSecs(jb.getString("thisSecs"));
          mileBeanList.add(mileBean);
        }
      } else {
        return null;
      }
    }
    return mileBeanList;
  }

  @Test
  public void test() {
    long start = LocalDateTime.of(2020, 8, 1, 0, 0, 1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    long end = LocalDateTime.of(2020, 8, 10, 0, 0, 1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    System.err.println(queryTrackData(
        "F6FA39393347F2B86E734D40396CDE93", start, end));
  }

  /**
   * 根据时间查询单个车辆轨迹数据
   *
   * @param id
   * @param startTime
   * @param endTime
   */
  @Override
  public List<TrackBean> queryTrackData(String id, Long startTime, Long endTime) {
    List<TrackBean> trackBeanList = new ArrayList<>();
    Map<String, Object> params = new HashMap<>(5);
    params.put("key", ThirdApiConfig.key);
    params.put("type", "1");
    params.put("id", id);
    params.put("startTime", String.valueOf(startTime));
    params.put("endTime", String.valueOf(endTime));
    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/track/queryTrackData.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONArray jsonArray = jsonObject.getJSONArray("obj");
      for (int i = 0; i < jsonArray.size(); i++) {
        TrackBean trackBean = new TrackBean();
        JSONObject jb = jsonArray.getJSONObject(i);
        trackBean.setDevTime(jb.getString("devTime"));
        trackBean.setLon(Double.parseDouble(jb.getString("lon")));
        trackBean.setLat(Double.parseDouble(jb.getString("lat")));
        /*trackBean.setAlarm(jb.getString("alarm"));
        trackBean.setIsRealTime(jb.getString("isRealTime"));
        trackBean.setMileage(jb.getString("mileage"));
        trackBean.setIsAcc(jb.getString("isAcc"));
        trackBean.setSpeed(jb.getString("speed"));
        trackBean.setIsPos(jb.getString("isPos"));
        trackBean.setStopTime(jb.getString("stopTime"));
        trackBean.setStopState(jb.getString("stopState"));
        trackBean.setOil(jb.getString("oil"));
        trackBean.setSensorSpeed(jb.getString("sensorSpeed"));
        trackBean.setTemperature(jb.getString("temperature"));
        trackBean.setScale(jb.getString("scale"));
        ExtendBean extendBean = new ExtendBean();
        JSONObject extendJson = jb.getJSONObject("extend");
        extendBean.setAcSignal(extendJson.getString("acSignal"));
        extendBean.setLbSignal(extendJson.getString("lbSignal"));
        extendBean.setHbSignal(extendJson.getString("hbSignal"));
        extendBean.setRtlSignal(extendJson.getString("rtlSignal"));
        extendBean.setLtlSignal(extendJson.getString("ltlSignal"));
        extendBean.setBrakeSignal(extendJson.getString("brakeSignal"));
        extendBean.setRSignal(extendJson.getString("RSignal"));
        extendBean.setHornSignal(extendJson.getString("hornSignal"));
        extendBean.setTurnDir(extendJson.getString("turnDir"));
        trackBean.setExtend(extendBean);*/
        trackBeanList.add(trackBean);
      }
    }
    return trackBeanList;
  }

  /**
   * 根据车牌号查询车辆动态数据
   *
   * @param plate
   */
  @Override
  public List<VehicleBean> getRealtimeDataByPlate(String plate) {
    List<VehicleBean> vehicleBeanList = new ArrayList<>();
    List<TireBean> tireBeanList = new ArrayList<>();
    VehicleBean vehicleBean;
    Map<String, Object> params = new HashMap<>(3);
    params.put("key", ThirdApiConfig.key);
    params.put("type", "1");
    params.put("plate", plate);
    HttpUtils httpUtils = new HttpUtils();
    String result = httpUtils.doGet(ThirdApiConfig.authIp + "/deviceData/realtimeDataByPlate.do", params);
    System.out.println("返回结果==" + result);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONArray jsonArray = jsonObject.getJSONArray("obj");
      for (int i = 0; i < jsonArray.size(); i++) {
        vehicleBean = new VehicleBean();
        JSONObject jb = jsonArray.getJSONObject(i);
        vehicleBean.setTerminalNo(jb.getString("devTime"));
        vehicleBean.setTerminalNo(jb.getString("terminalNo"));
        vehicleBean.setPlateNo(jb.getString("plate"));
        vehicleBean.setVehicleDeviceId(jb.getString("id"));
        vehicleBean.setTerminalNo(jb.getString("terminalType"));
        vehicleBean.setIsPos(jb.getString("isPos"));
        vehicleBean.setSensorSpeed(jb.getString("sensorSpeed"));
        vehicleBean.setAcc(jb.getString("acc"));
        vehicleBean.setTemperature(jb.getString("temperature"));
        vehicleBean.setOil(jb.getString("oil"));
        vehicleBean.setScale(jb.getString("scale"));
        vehicleBean.setFormatTime(jb.getString("formatTime"));
        vehicleBean.setDirect(jb.getString("direct"));
        vehicleBean.setLon(jb.getString("lon"));
        vehicleBean.setLat(jb.getString("lat"));
        vehicleBean.setGpsTime(jb.getString("gpsTime"));
        vehicleBean.setAlarmInfo(jb.getString("alarmInfo"));
        vehicleBean.setSpeed(jb.getString("speed"));
        vehicleBean.setMlileage(jb.getString("mlileage"));
        vehicleBean.setVehicleDeviceStatus(Integer.parseInt(jb.getString("vehicleStatus")));
        vehicleBean.setLbSignal(jb.getString("lbSignal"));
        vehicleBean.setRtlSignal(jb.getString("rtlSignal"));
        vehicleBean.setLtlSignal(jb.getString("ltlSignal"));
        vehicleBean.setBrakeSignal(jb.getString("brakeSignal"));
        vehicleBean.setRSignal(jb.getString("RSignal"));
        vehicleBean.setHornSignal(jb.getString("hornSignal"));
        vehicleBean.setAcSignal(jb.getString("acSignal"));
        vehicleBean.setNetModel(jb.getString("netModel"));
        vehicleBean.setTurnDir(jb.getString("turnDir"));
        JSONArray tireArray = jb.getJSONArray("tirePressureInfo");
        for (int j = 0; j < tireArray.size(); j++) {
          TireBean tireBean = new TireBean();
          JSONObject object = tireArray.getJSONObject(j);
          tireBean.setStatus(object.getString("status"));
          tireBean.setAlarmStatus(object.getString("alarmStatus"));
          tireBean.setTyrePemperature(object.getString("tyrePemperature"));
          tireBean.setTyrePressure(object.getString("tyrePressure"));
          tireBean.setTyreIdentifier(object.getString("tyreIdentifier"));
          tireBeanList.add(tireBean);
        }
        vehicleBean.setTireBeans(tireBeanList);
        vehicleBeanList.add(vehicleBean);
      }
    }

    return vehicleBeanList;
  }

  /**
   * 查询实时ADAS报警
   */
  @Override
  public AsdsBean getRealTimeAdasAlarm(Long time, String type) {
    return null;
  }

  /**
   * 查询ADAS报警历史记录
   */

  @Override
  public AsdsBean getAdasAlarmReport(String adasType, Long startTime, Long endTime) {
    return null;
  }

  @Override
  public void getAdasAlarmDetail() {

  }

  @Override
  public void getQueryImage() {

  }

  @Override
  public void getDownloadImage() {

  }
}
