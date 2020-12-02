package com.yuntun.sanitationkitchen.vehicle.api;

import com.alibaba.fastjson.JSONObject;
import com.yuntun.sanitationkitchen.util.HttpUtils;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HolidayApi {

  // @SetCache("HOLIDAY#${month}")
  public Map<LocalDate, Boolean> getHolidayAndMarkUpDay(String month) {
    Map<LocalDate, Boolean> resultHoliday = null;

    String result = HttpUtils.doGet("http://timor.tech/api/holiday/year/" + month);
    if (result != null) {
      resultHoliday = new HashMap<>();
      JSONObject jsonObject = JSONObject.parseObject(result);
      JSONObject jsonObjectHoliday = jsonObject.getJSONObject("holiday");
      Map<String, Object> resultMap = jsonObjectHoliday.getInnerMap();
      for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
        Map<String, Object> eachResult = (Map<String, Object>) JSONObject.parse(JSONObject.toJSONString(entry.getValue()));
        resultHoliday.put(LocalDate.parse((String) eachResult.get("date")), (Boolean) eachResult.get("holiday"));

      }
    }
    return resultHoliday;
  }

  public static void main(String[] args) {
    System.out.println(new HolidayApi().getHolidayAndMarkUpDay("2020-05"));
  }
}
