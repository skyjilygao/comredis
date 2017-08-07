package com.sky.commons;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.collections.MapUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
 
/**
 *
 * @author skyjilygao
 * @version
 */
public class EntityUtils {
 
    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ByteConverter(null), Byte.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
    }
 
    public static Map<String, String> objectToHash(Object obj) {
        try {
            Map<String, String> map = Maps.newHashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {
                    if (property.getReadMethod().invoke(obj) != null) {
                        map.put(property.getName(), "" + property.getReadMethod().invoke(obj));
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    @SuppressWarnings("unchecked")
    private static void hashToObject(Map<?, ?> map, Object obj) {
        try {
            BeanUtils.populate(obj, (Map)map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    @SuppressWarnings("unchecked")
    public static <T> T hashToObject(Map<?, ?> map, Class c) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        try {
            Object obj = c.newInstance();
            hashToObject(map, obj);
            return (T)obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}