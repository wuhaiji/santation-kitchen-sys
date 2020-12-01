package com.yuntun.sanitationkitchen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/1
 */
public class ListUtil {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    /**
     * 将list中的对象转为另一个有相同属性的对象的list
     * 主要是用于entity转vo
     *
     * @param tClass 要转成想要的对象class
     * @param list   源list
     * @param <T>    泛型对象
     * @return
     */
    public static <T> List<T> listMap(Class<T> tClass, List<?> list) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        return list.parallelStream()
                .map(
                        i -> {
                            T vehicle;
                            try {
                                vehicle = tClass.newInstance();
                            } catch (Exception e) {
                                log.error("Exception:", e);
                                return null;
                            }
                            BeanUtils.copyProperties(i, vehicle);
                            return vehicle;
                        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
