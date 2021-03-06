package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
public class PermissionListVo {
    Long uid;
    Long parentId;
    String permissionName;
    String permissionTag;
    @JsonFormat(pattern = DateConst.dateTimePattern)
    LocalDateTime createTime;
    Long creator;
}
