package com.yuntun.sanitationkitchen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @author: yookfeng
 * @date: 2020-06-06 14:12
 * @description 日期工具类
 */
public class FormatDateUtils {

  /***
   * 在参数传入日期上加一天
   * @param dateParam
   */
  public static String addOneDay(String dateParam) {

    String dateEnd;
    Date date = null;
    try {
      if (dateParam != null) {
        date = org.apache.commons.lang3.time.DateUtils.parseDate(dateParam, "yyyy-MM-dd");
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    date = org.apache.commons.lang3.time.DateUtils.addDays(date, 1);
    dateEnd = DateFormatUtils.format(date, "yyyy-MM-dd");

    return dateEnd;
  }

  /**
   * localDate转时间戳
   *
   * @param localDate
   * @return
   */
  public static Long LocalDate2TimeStamp(LocalDate localDate) {
    //获取系统默认时区
    ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
    //获取日期时间戳,utc
    return localDate.atStartOfDay().toInstant(offset).toEpochMilli();
  }


  public static String getDate() {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    String date = sf.format(calendar.getTime());
    return date;
  }

  public static String getNextDay() {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    String nextDay = sf.format(calendar.getTime());
    return nextDay;
  }

  public static LocalDate getDay2(String dateStr) {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    Date date = null;
    try {
      date = sf.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    String day2 = sf.format(calendar.getTime());

    return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

  }
}
