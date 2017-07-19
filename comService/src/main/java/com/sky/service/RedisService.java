package com.sky.service;

import org.springframework.data.redis.core.ListOperations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheValue(String k, String v, long time);

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheValue(String k, String v);

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    boolean containsValueKey(String k);

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    boolean containsSetKey(String k);

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    boolean containsListKey(String k);

    boolean containsKey(String key);

    /**
     * 获取缓存
     *
     * @param k
     * @return
     */
    String getValue(String k);

    /**
     * 获取int型缓存
     *
     * @param k
     * @return
     */
    int getIntValue(String k);

    /**
     * 移除缓存
     *
     * @param k
     * @return
     */
    boolean removeValue(String k);

    boolean removeSet(String k);

    boolean removeList(String k);

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     * 缓存set操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheSet(String k, String v, long time);

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheSet(String k, String v);

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheSet(String k, Set<String> v, long time);

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheSet(String k, Set<String> v);

    /**
     * 获取缓存set数据
     *
     * @param k
     * @return
     */
    Set<String> getSet(String k);

    /**
     * 获取Int Set
     *
     * @param k
     * @return
     */
    Set<Integer> getIntSet(String k);

    /**
     * list缓存
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheList(String k, String v, long time);

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheList(String k, String v);

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheList(String k, List<String> v, long time);

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheList(String k, List<String> v);

    /**
     * 获取list缓存
     *
     * @param k
     * @param start
     * @param end
     * @return
     */
    List<String> getList(String k, long start, long end);

    /**
     * 获取总条数, 可用于分页
     *
     * @param k
     * @return
     */
    long getListSize(String k);

    /**
     * 获取总条数, 可用于分页
     *
     * @param listOps
     * @param k
     * @return
     */
    long getListSize(ListOperations<String, String> listOps, String k);

    /**
     * 移除list缓存
     *
     * @param k
     * @return
     */
    boolean removeOneOfList(String k);

    /**
     * 缓存hash操作
     *
     * @param k
     * @param hk
     * @param v
     * @param time
     * @return
     */
    boolean cacheHash(String k, String hk, String v, long time);

    /**
     * 缓存hash
     *
     * @param k
     * @param hk
     * @param v
     * @return
     */
    boolean cacheHash(String k, String hk, String v);

    /**
     * 缓存hash
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheHash(String k, Map<String, String> v, long time);

    /**
     * 缓存hash
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheHash(String k, Map<String, String> v);

    /**
     * 获取缓存hash数据
     *
     * @param k
     * @return
     */
    Map<String, String> getHash(String k);

    /**
     * 缓存Object操作
     *
     * @param k
     * @param obj
     * @param time
     * @return
     */
    <T extends Serializable> boolean setObject(String k, T obj, long time);

    /**
     * 缓存Object操作
     *
     * @param k
     * @param obj
     * @return
     */
    <T extends Serializable> boolean setObject(String k, T obj);

    /**
     * 获取缓存
     *
     * @param obj
     * @return
     */
    <T extends Serializable> T getObject(String obj, Class<T> clazz);

    /**
     * 缓存序列化List操作
     *
     * @param k
     * @param obj
     * @param clazz
     * @param time
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setList(String k, List<T> obj, Class<T> clazz, long time);

    /**
     * 缓存序列化List操作
     *
     * @param k
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setList(String k, List<T> obj, Class<T> clazz);

    /**
     * 获取序列化List
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> List<T> getList(String obj, Class<T> clazz);

    /**
     * 缓存序列化Map
     *
     * @param obj
     * @param clazz
     * @param time
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setMap(String k, Map<String, T> obj, Class<T> clazz, long time);

    /**
     * 缓存序列化Map
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setMap(String k, Map<String, T> obj, Class<T> clazz);

    /**
     * 获取序列化Map
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> Map<String, T> getMap(String obj, Class<T> clazz);

    /**
     * 缓存序列化Set
     *
     * @param k
     * @param obj
     * @param clazz
     * @param time
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setSet(String k, Set<T> obj, Class<T> clazz, long time);

    /**
     * 缓存序列化Set
     *
     * @param k
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> boolean setSet(String k, Set<T> obj, Class<T> clazz);

    /**
     * 获取序列化Set
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Serializable> Set<T> getSet(String obj, Class<T> clazz);

}
