package  com.yuntun.sanitationkitchen.api;



import com.yuntun.sanitationkitchen.bean.AsdsBean;
import com.yuntun.sanitationkitchen.bean.MileBean;
import com.yuntun.sanitationkitchen.bean.TrackBean;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import java.util.List;

/**
 * @author yookfeng 2020/8/17
 * @des 车辆接口
 **/
public interface IVehicle {

  /**
   * 查询车辆信息
   *
   * @return
   */
  List<VehicleBean> getVehicleInfo();

  /**
   * 获取车辆实时信息
   *
   * @param ids
   * @return
   */
  List<VehicleBean> getVehicleRealtimeData( String ids);

  /**
   * 查询用户下所有车实时当前状态数据
   *
   * @return
   */
  List<VehicleBean> getAllRealtimeData();

  /**
   * 查询单个车辆里程数据
   *
   * @param id
   * @param startTime
   * @param endTime
   * @return
   */
  List<MileBean>  getMileageData( String id, Long startTime, Long endTime);

  /**
   * 根据时间查询单个车辆轨迹数据
   *
   * @param id
   * @param startTime
   * @param endTime
   */
  List<TrackBean> queryTrackData(String id, Long startTime, Long endTime);

  /**
   * 根据车牌号查询车辆动态数据
   *
   * @param plate
   */
  List<VehicleBean> getRealtimeDataByPlate(String plate);

  /**
   * 查询实时ADAS报警
   */
  AsdsBean getRealTimeAdasAlarm( Long time, String type);

  /**
   * 查询ADAS报警历史记录
   */

  AsdsBean getAdasAlarmReport(String adasType, Long startTime, Long endTime);

  void getAdasAlarmDetail();

  void getQueryImage();

  void getDownloadImage();
}
