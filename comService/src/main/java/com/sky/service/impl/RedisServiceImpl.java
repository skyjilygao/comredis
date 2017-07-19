package com.sky.service.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.sky.service.RedisService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue(String k, String v, long time) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheValue(String k, String v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsValueKey(String k) {
        return containsKey(k);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsSetKey(String k) {
        return containsKey(k);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsListKey(String k) {
        return containsKey(k);
    }

    public boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param k
     * @return
     */
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            return valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 获取int型缓存
     *
     * @param k
     * @return
     */
    public int getIntValue(String k) {
        return Integer.parseInt(getValue(k));
    }

    /**
     * 移除缓存
     *
     * @param k
     * @return
     */
    public boolean removeValue(String k) {
        return remove(k);
    }

    public boolean removeSet(String k) {
        return remove(k);
    }

    public boolean removeList(String k) {
        return remove(k);
    }

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 缓存set操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, String v, long time) {
        String key = k;
        try {
            SetOperations<String, String> valueOps = redisTemplate.opsForSet();
            valueOps.add(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, String v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, Set<String> v, long time) {
        String key = k;
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, v.toArray(new String[v.size()]));
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, Set<String> v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 获取缓存set数据
     *
     * @param k
     * @return
     */
    public Set<String> getSet(String k) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(k);
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    @Override
    public Set<Integer> getIntSet(String k) {
        try {
            Set<Integer> result = redisTemplate.boundSetOps(k).members();
            return result;
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * list缓存
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, String v, long time) {
        String key = k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPush(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, String v) {
        return cacheList(k, v, -1);
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, List<String> v, long time) {
        String key = k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            long l = listOps.rightPushAll(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, List<String> v) {
        return cacheList(k, v, -1);
    }

    /**
     * 获取list缓存
     *
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<String> getList(String k, long start, long end) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.range(k, start, end);
        } catch (Throwable t) {
            logger.error("获取list缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param k
     * @return
     */
    public long getListSize(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.size(k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param listOps
     * @param k
     * @return
     */
    public long getListSize(ListOperations<String, String> listOps, String k) {
        try {
            return listOps.size(k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 移除list缓存
     *
     * @param k
     * @return
     */
    public boolean removeOneOfList(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPop(k);
            return true;
        } catch (Throwable t) {
            logger.error("移除list缓存失败key[" + k + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 缓存hash操作
     *
     * @param k
     * @param hk
     * @param v
     * @param time
     * @return
     */
    public boolean cacheHash(String k, String hk, String v, long time) {
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            hashOps.put(k, hk, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存hash
     *
     * @param k
     * @param hk
     * @param v
     * @return
     */
    public boolean cacheHash(String k, String hk, String v) {
        return cacheHash(k, hk, v, -1);
    }

    /**
     * 缓存hash
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheHash(String k, Map<String, String> v, long time) {
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            hashOps.putAll(k, v);
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存hash
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheHash(String k, Map<String, String> v) {
        return cacheHash(k, v, -1);
    }

    /**
     * 获取缓存hash数据
     *
     * @param k
     * @return
     */
    public Map<String, String> getHash(String k) {
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            return hashOps.entries(k);
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param obj
     * @param time
     * @return
     */
    @Override
    public <T extends Serializable> boolean setObject(String k, T obj, long time) {
        String key = k;
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.register(obj.getClass(), new JavaSerializer());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            kryo.writeClassAndObject(output, obj);
            output.flush();
            output.close();

            byte[] b = baos.toByteArray();
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            valueOps.set(key, new String(new Base64().encode(b)));
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + obj + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param obj
     * @return
     */
    @Override
    public <T extends Serializable> boolean setObject(String k, T obj) {
        return setObject(k, obj, -1);
    }

    @Override
    public <T extends Serializable> T getObject(String k, Class<T> clazz) {
        String obj = "";
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            obj = valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }

        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(clazz, new JavaSerializer());

        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj));
        Input input = new Input(bais);
        return (T) kryo.readClassAndObject(input);
    }

    @Override
    public <T extends Serializable> boolean setList(String k, List<T> obj, Class<T> clazz, long time) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(true);

            CollectionSerializer serializer = new CollectionSerializer();
            serializer.setElementClass(clazz, new JavaSerializer());
            serializer.setElementsCanBeNull(false);

            kryo.register(clazz, new JavaSerializer());
            kryo.register(ArrayList.class, serializer);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            kryo.writeObject(output, obj);
            output.flush();
            output.close();

            byte[] b = baos.toByteArray();
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            valueOps.set(k, new String(new Base64().encode(b)));
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + obj + "]", t);
        }
        return false;
    }

    @Override
    public <T extends Serializable> boolean setList(String k, List<T> obj, Class<T> clazz) {
        return setList(k, obj, clazz, -1);
    }

    @Override
    public <T extends Serializable> List<T> getList(String k, Class<T> clazz) {
        String obj = "";
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            obj = valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }

        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);

        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);

        kryo.register(clazz, new JavaSerializer());
        kryo.register(ArrayList.class, serializer);

        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj));
        Input input = new Input(bais);
        return (List<T>) kryo.readObject(input, ArrayList.class, serializer);
    }

    @Override
    public <T extends Serializable> boolean setMap(String k, Map<String, T> obj, Class<T> clazz, long time) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(true);

            MapSerializer serializer = new MapSerializer();
            serializer.setKeyClass(String.class, new JavaSerializer());
            serializer.setKeysCanBeNull(false);
            serializer.setValueClass(clazz, new JavaSerializer());
            serializer.setValuesCanBeNull(true);

            kryo.register(clazz, new JavaSerializer());
            kryo.register(HashMap.class, serializer);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            kryo.writeObject(output, obj);
            output.flush();
            output.close();

            byte[] b = baos.toByteArray();
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            valueOps.set(k, new String(new Base64().encode(b)));
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + obj + "]", t);
        }
        return false;
    }

    @Override
    public <T extends Serializable> boolean setMap(String k, Map<String, T> obj, Class<T> clazz) {
        return setMap(k, obj, clazz, -1);
    }

    @Override
    public <T extends Serializable> Map<String, T> getMap(String k, Class<T> clazz) {
        String obj = "";
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            obj = valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }

        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);

        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(String.class, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(clazz, new JavaSerializer());
        serializer.setValuesCanBeNull(true);

        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashMap.class, serializer);

        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj));
        Input input = new Input(bais);
        return (Map<String, T>) kryo.readObject(input, HashMap.class, serializer);
    }

    @Override
    public <T extends Serializable> boolean setSet(String k, Set<T> obj, Class<T> clazz, long time) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(true);

            CollectionSerializer serializer = new CollectionSerializer();
            serializer.setElementClass(clazz, new JavaSerializer());
            serializer.setElementsCanBeNull(false);

            kryo.register(clazz, new JavaSerializer());
            kryo.register(HashSet.class, serializer);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            kryo.writeObject(output, obj);
            output.flush();
            output.close();

            byte[] b = baos.toByteArray();
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            valueOps.set(k, new String(new Base64().encode(b)));
            if (time > 0) redisTemplate.expire(k, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + k + "]失败, value[" + obj + "]", t);
        }
        return false;
    }

    @Override
    public <T extends Serializable> boolean setSet(String k, Set<T> obj, Class<T> clazz) {
        return setSet(k, obj, clazz, -1);
    }

    @Override
    public <T extends Serializable> Set<T> getSet(String k, Class<T> clazz) {
        String obj = "";
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            obj = valueOps.get(k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }

        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);

        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);

        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashSet.class, serializer);

        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj));
        Input input = new Input(bais);
        return (Set<T>) kryo.readObject(input, HashSet.class, serializer);
    }

}
