package com.yuntun.sanitationkitchen.vehicle.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yuntun.sanitationkitchen.bean.*;
import com.yuntun.sanitationkitchen.config.ApiStatusConfig;
import com.yuntun.sanitationkitchen.config.ThirdApiConfig;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.HttpUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author yookfeng 2020/8/17
 * @des 车辆API
 **/
@Component
public class VehicleApi implements IVehicle {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    public static final String realtimeDataByPlateUrl = "/deviceData/realtimeDataByPlate.do";
    public static final String realtimeDataByIdsUrl = "/deviceData/realtimeData.do";
    public static final String videoVehicleUrl = "/vehicle/getVehicleByVender.do";
    public static final String KEY = "key";
    public static final String IDS = "ids";
    public static final String PLATE = "plate";
    public static final String CUSTOMER_KEY = "customerKey";
    public static final String TYPE = "type";

    // @Autowired
    ThirdApiConfig thirdApiConfig
            = new ThirdApiConfig()
            .setAuthIp("http://120.77.112.76:6809")
            .setKey("85f79a11-8770-4b4b-86a7-fc964bbbfb0f")
            .setVideoUrl("https://vserver.car900.com");

    public static void main(String[] args) {

        // // 根据车牌号查询车辆动态数据
        // ArrayList<String> plates = new ArrayList<String>() {{
        //     add("13302690436");
        // }};
        // List<VehicleVideoDto> vehicleVideoDtoList = new VehicleApi().listVideoVehicle();
        // Long startTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,17,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // Long endTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,18,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //
        // System.out.println("startTime:"+LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,16,0,0)));
        // System.out.println("endTime:"+LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,19,0,0)));


        // List<TrackBean> trackBeans = new VehicleApi().queryTrackData(
        //         "F6FA39393347F2B86E734D40396CDE93",
        //         new Date(120, 7, 17).getTime(),
        //         new Date(120, 7, 18).getTime()
        // );

        // List<TrackBean> trackBeans = new VehicleApi().queryTrackData(
        //         "F6FA39393347F2B86E734D40396CDE93",
        //         startTime,
        //         endTime
        // );
        VehicleApi vehicleApi = new VehicleApi();
        List<VehicleBean> list = vehicleApi.list();
        // System.out.println(JSONArray.toJSON(list));
    }

    /**
     * 处理oil字段
     *
     * @param response
     * @return
     */
    private JSONObject handleOilValue(String response) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("obj");
        if (jsonArray == null)
            return jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject status = jsonArray.getJSONObject(i);
            Object oil = status.get("oil");
            status.put("oil", oil.toString());
            jsonArray.set(i, status);
        }
        jsonObject.put("obj", jsonArray);
        return jsonObject;
    }

    /**
     * 处理返回msg
     *
     * @param response
     * @param resultDto
     * @return
     */
    private boolean checkResponse(String response, AdasResultDto<?> resultDto) {
        if (AdasResultDto.FLAG_ERROR == resultDto.getFlag()) {
            log.error(
                    "vehicle api->?->api调用返回失败消息，msg:{},response:{}",
                    resultDto.getMsg(),
                    response
            );
            //直接异常
            return false;
        }
        return true;
    }

    /**
     * 查询车辆信息
     *
     * @return
     */
    @Override
    public List<VehicleBean> list() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("key", thirdApiConfig.getKey());

        String result = HttpUtil.get(thirdApiConfig.getAuthIp() + "/deviceData/vehicleBaseInfo.do", params);
        System.out.println("list->:" + result);

        List<VehicleBean> vehicleBeans = null;
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String obj = jsonObject.getString("obj");
            if (obj != null) {
                vehicleBeans = JSONObject.parseArray(obj, VehicleBean.class);

            } else {
                log.error("来源云查询车辆列表异常");
                vehicleBeans = new ArrayList<>();
            }
        }
        return vehicleBeans;
    }


    /**
     * 获取车辆s实时信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<VehicleBean> getVehicleRealtimeData(List<String> ids) {
        if (ids.size() <= 0) {
            return new ArrayList<>();
        }
        List<VehicleBean> vehicleBeanList = new ArrayList<>();
        VehicleBean vehicleBean;
        Map<String, Object> params = new HashMap<>(3);
        params.put("key", thirdApiConfig.getKey());
        params.put("ids", String.join(",", ids));
        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.doGet(thirdApiConfig.getAuthIp() + "/deviceData/realtimeData.do", params);
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
        params.put("key", thirdApiConfig.getKey());
        params.put("type", "1");
        params.put("filterTime", String.valueOf(System.currentTimeMillis()));

        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.doGet(thirdApiConfig.getAuthIp() + "/deviceData/allRealData.do", params);
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
        params.put("key", thirdApiConfig.getKey());
        params.put("type", "1");
        params.put("id", id);
        params.put("startTime", String.valueOf(System.currentTimeMillis()));
        params.put("endTime", String.valueOf(System.currentTimeMillis()));
        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.doGet(thirdApiConfig.getAuthIp() + "/mileage/mileageData.do", params);
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
        Map<String, Object> params = new HashMap<>(5);
        params.put("key", thirdApiConfig.getKey());
        params.put("type", "1");
        params.put("id", id);
        params.put("startTime", String.valueOf(startTime));
        params.put("endTime", String.valueOf(endTime));
        String result = HttpUtil.get(thirdApiConfig.getAuthIp() + "/track/queryTrackData.do", params);
        System.out.println("response:"+result);
        AdasResultDto<List<TrackBean>> resultDto = JSONObject.parseObject(
                result,
                new TypeReference<AdasResultDto<List<TrackBean>>>() {}
        );
        //检查返回msg
        boolean b = checkResponse(result, resultDto);
        if (!b) {
            return new ArrayList<>();
        }
        return resultDto.getObj();
        // List<String> jsonArray = resultDto.getObj();
        // for (int i = 0; i < jsonArray.size(); i++) {
        //     TrackBean trackBean = new TrackBean();
        //     JSONObject jb = JSONObject.parseObject(jsonArray.get(i));
        //     trackBean.setDevTime(jb.getString("devTime"));
        //     trackBean.setLon(Double.parseDouble(jb.getString("lon")));
        //     trackBean.setLat(Double.parseDouble(jb.getString("lat")));
        //     trackBean.setSpeed(jb.getDouble("speed"));
        // /*trackBean.setAlarm(jb.getString("alarm"));
        // trackBean.setIsRealTime(jb.getString("isRealTime"));
        // trackBean.setMileage(jb.getString("mileage"));
        // trackBean.setIsAcc(jb.getString("isAcc"));
        // trackBean.setSpeed(jb.getString("speed"));
        // trackBean.setIsPos(jb.getString("isPos"));
        // trackBean.setStopTime(jb.getString("stopTime"));
        // trackBean.setStopState(jb.getString("stopState"));
        // trackBean.setOil(jb.getString("oil"));
        // trackBean.setSensorSpeed(jb.getString("sensorSpeed"));
        // trackBean.setTemperature(jb.getString("temperature"));
        // trackBean.setScale(jb.getString("scale"));
        // ExtendBean extendBean = new ExtendBean();
        // JSONObject extendJson = jb.getJSONObject("extend");
        // extendBean.setAcSignal(extendJson.getString("acSignal"));
        // extendBean.setLbSignal(extendJson.getString("lbSignal"));
        // extendBean.setHbSignal(extendJson.getString("hbSignal"));
        // extendBean.setRtlSignal(extendJson.getString("rtlSignal"));
        // extendBean.setLtlSignal(extendJson.getString("ltlSignal"));
        // extendBean.setBrakeSignal(extendJson.getString("brakeSignal"));
        // extendBean.setRSignal(extendJson.getString("RSignal"));
        // extendBean.setHornSignal(extendJson.getString("hornSignal"));
        // extendBean.setTurnDir(extendJson.getString("turnDir"));
        // trackBean.setExtend(extendBean);*/
        //     trackBeanList.add(trackBean);
        // }
        // return trackBeanList;
    }

    /**
     * 根据车牌号查询车辆动态数据
     *
     * @param plates
     */
    @Override
    public List<VehicleBean> getRealtimeDataByPlate(List<String> plates) {
        List<VehicleBean> vehicleBeanList = new ArrayList<>();
        List<TireBean> tireBeanList = new ArrayList<>();
        VehicleBean vehicleBean;
        Map<String, Object> params = new HashMap<>(3);
        params.put(KEY, thirdApiConfig.getKey());
        params.put("type", "1");
        params.put(PLATE, String.join(",", plates));
        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.doGet(thirdApiConfig.getAuthIp() + "/deviceData/realtimeDataByPlate.do", params);
        System.out.println("返回结果==" + result);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("obj");
            for (int i = 0; i < jsonArray.size(); i++) {
                vehicleBean = new VehicleBean();
                JSONObject jb = jsonArray.getJSONObject(i);
                vehicleBean.setTerminalNo(jb.getString("devTime"));
                vehicleBean.setTerminalNo(jb.getString("terminalNo"));
                vehicleBean.setPlate(jb.getString("plate"));
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
     * 根据车辆ids查询车辆动态数据
     *
     * @param ids 车俩 ids
     * @return
     */
    @Override
    public List<VehicleRealtimeStatusAdasDto> ListVehicleRealtimeStatusByIds(List<String> ids) {

        log.info("vehicle api->ListVehicleRealtimeStatusByIds->params:{}", ids);

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(KEY, thirdApiConfig.getKey());
        paramsMap.put(IDS, String.join(",", ids));

        String response = HttpUtil.post(thirdApiConfig.getAuthIp() + realtimeDataByIdsUrl, paramsMap);
        log.info("vehicle api->ListVehicleRealtimeStatusByIds,response:{}", response);

        if (EptUtil.isEmpty(response)) {
            log.error("vehicle api->ListVehicleRealtimeStatusByIds->api调用无返回信息");
            return new ArrayList<>();
        }

        JSONObject jsonObject = handleOilValue(response);


        AdasResultDto<List<VehicleRealtimeStatusAdasDto>> resultDto = JSONObject.parseObject(
                jsonObject.toJSONString(),
                new TypeReference<AdasResultDto<List<VehicleRealtimeStatusAdasDto>>>() {
                }
        );

        boolean b = checkResponse(response, resultDto);
        if (b) {
            return resultDto.getObj();
        }
        return new ArrayList<>();
    }

    /**
     * 根据车牌号查询车辆动态数据
     *
     * @param plates 车牌号list
     * @return
     */
    @Override
    public List<VehicleRealtimeStatusAdasDto> ListVehicleRealtimeStatusByPlates(List<String> plates) {


        log.info("vehicle api->ListVehicleRealtimeStatusByPlates->params:{}", plates);

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(KEY, thirdApiConfig.getKey());
        paramsMap.put(PLATE, String.join(",", plates));

        String response = HttpUtil.post(thirdApiConfig.getAuthIp() + realtimeDataByPlateUrl, paramsMap);
        log.info("vehicle api->ListVehicleRealtimeStatusByPlates,response:{}", response);

        if (EptUtil.isEmpty(response)) {
            log.error("vehicle api->ListVehicleRealtimeStatusByPlates->api调用无返回信息,response:{}", response);
            return new ArrayList<>();
        }


        JSONObject jsonObject = handleOilValue(response);
        String text = jsonObject.toJSONString();

        AdasResultDto<List<VehicleRealtimeStatusAdasDto>> resultDto = JSONObject.parseObject(
                text,
                new TypeReference<AdasResultDto<List<VehicleRealtimeStatusAdasDto>>>() {
                }
        );

        boolean b = checkResponse(response, resultDto);
        if (b) {
            return resultDto.getObj();
        }
        return new ArrayList<>();

    }

    @Override
    public List<VehicleVideoDto> listVideoVehicle() {

        log.info("vehicle api->listVideoVehicle");

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(CUSTOMER_KEY, thirdApiConfig.getKey());
        paramsMap.put(TYPE, "1");

        String response = HttpUtil.get(thirdApiConfig.getVideoUrl() + videoVehicleUrl, paramsMap);
        log.info("vehicle api->listVideoVehicle,response:{}", response);

        if (EptUtil.isEmpty(response)) {
            log.error("vehicle api->listVideoVehicle->api调用无返回信息,response:{}", response);
            return new ArrayList<>();
        }
        AdasResultDto<List<VehicleVideoDto>> resultDto = JSONObject.parseObject(
                response,
                new TypeReference<AdasResultDto<List<VehicleVideoDto>>>() {
                }
        );
        boolean b = checkResponse(response, resultDto);
        if (b) {
            return resultDto.getObj();
        }
        return new ArrayList<>();
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
