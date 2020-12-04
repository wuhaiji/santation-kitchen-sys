package com.yuntun.sanitationkitchen.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类，使用之前请确保RedisTemplate成功注入
 *
 * @author whj
 */
public class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);
    // jackson转换工具
    private static final ObjectMapper jackson = new ObjectMapper()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    private RedisUtils() {
    }

    @SuppressWarnings("unchecked")
    private static final RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("myRedisTemplate");

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expireSeconds(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    /**
     * 删除单个key
     *
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean delKey(final String key) {

        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }

    /**
     * 删除多个key
     *
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static long delKeys(final Collection<String> keys) {

        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }

    /**
     * 存入普通对象，不設置过期
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void setValue(final String key, final Object value) {

        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置lock，新方法
     *
     * @param key     锁key
     * @param outTime 超时时间，单位毫秒
     * @return
     */
    public synchronized static Boolean setLockNx(final String key, Long outTime) {
        return redisTemplate.opsForValue().setIfAbsent(key, 1, outTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置lock，新方法
     *
     * @param key      锁key
     * @param outTime  超时时间，单位
     * @param timeUnit 单位
     * @return 加锁是否成功
     */
    public synchronized static Boolean setLockNx(final String key, Long outTime, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, 1, outTime, timeUnit);
    }

    /**
     * 删除lock
     *
     * @param target Redis键
     */
    public synchronized static void unLock(final String target) {
        try {
            Boolean ret = redisTemplate.delete(target);
            if (ret == null) {
                log.error("警报！redis解锁异常");
            }
        } catch (Exception e) {
            log.error("警报！redis解锁异常", e);
            e.printStackTrace();
        }
    }

    /**
     * 存入普通对象,10分钟过期
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void setValueTenMinutes(final String key, final Object value) {

        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
    }

    // 存储普通对象操作

    /**
     * 存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位秒
     */
    public static void setValueExpireSeconds(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    /**
     * 存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位毫秒
     */
    public static void setValueExpireMills(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }
    /**
     * 存入普通对象
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效期，单位秒
     */
    public static void setValueExpire(final String key, final Object value, final long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public static Object getValue(final String key) {

        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取键的存活剩余时间 -2表示不存在 ，-1 key 存在但没有设置剩余生存时间时
     *
     * @param key 键
     * @return 对象
     */
    public static Long getExpireTimePTtl(final String key) {
        Long pTtl = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.pTtl(key.getBytes()));

        if (pTtl == null || pTtl == -2) {
            return null;
        }
        return pTtl;
    }

    /**
     * 获取键的存活剩余时间 -2表示不存在 ，-1 key 存在但没有设置剩余生存时间时
     *
     * @param key 键
     * @return 对象
     */
    public static Long getExpireTimeTTl(final String key) {
        Long ttl = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.ttl(key.getBytes()));

        if (ttl == null || ttl == -2) {
            return null;
        }
        return ttl;
    }

    /**
     * 获取string值     *
     *
     * @param key 键
     * @return 对象
     */
    public static String getString(final String key) {
        String string;
        try {
            string = (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis获取string值异常");
        }
        return string;
    }

    /**
     * 获取string值     *
     *
     * @param key 键
     * @return 对象
     */
    public static <T> T getObject(final String key, Class<T> tClass) {
        T t;
        try {
            String string = (String) redisTemplate.opsForValue().get(key);
            if(string==null){
                return null;
            }
            t = jackson.readValue(string, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis获取Object值异常");
        }
        return t;
    }
    // 存储Hash操作

    /**
     * 确定哈希hashKey是否存在
     *
     * @param key  键
     * @param hkey hash键
     * @return true=存在；false=不存在
     */
    public static boolean hasHashKey(final String key, String hkey) {

        Boolean ret = redisTemplate.opsForHash().hasKey(key, hkey);
        return ret != null && ret;
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static void hashPut(final String key, final String hKey, final Object value) {

        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 往Hash中存入多个数据
     *
     * @param key    Redis键
     * @param values Hash键值对
     */
    public static void hashPutAll(final String key, final Map values) {
        redisTemplate.opsForHash().putAll(key, values);
    }


    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static Object hashGet(final String key, final String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static Object hashGetRandom(final String key, final String hKey) {
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @return Hash对象
     */
    public static Map<Object, Object> hashGetAll(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hashMultiGet(final String key, final Collection<Object> hKeys) {

        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static long hashDeleteKeys(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().delete(key, hKeys);
    }

    // 存储Set相关操作

    /**
     * 往Set中存入数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long setSet(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除Set中的数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long setDel(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 获取set中的所有对象
     *
     * @param key Redis键
     * @return set集合
     */
    public static Set<Object> getSetAll(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // 存储ZSet相关操作

    /**
     * 往ZSet中存入数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long zsetSet(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除ZSet中的数据
     *
     * @param key    Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long zsetDel(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }

    // 存储List相关操作

    /**
     * 往List中存入数据
     *
     * @param key   Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public static long listPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(final String key, final Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key   Redis键
     * @param start 开始位置
     * @param end   结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public static List<Object> listGet(final String key, final int start, final int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 从List中获取begin到end之间的元素
     * start=0，end=-1表示获取全部元素
     *
     * @param key Redis键
     * @return List对象
     */
    public static List<Object> listGetAll(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

}
