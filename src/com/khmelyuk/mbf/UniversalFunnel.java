package com.khmelyuk.mbf;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/** Funnel for everything, uses reflection for objects */
public class UniversalFunnel implements Funnel<Object> {

    @Override public void funnel(Object from, PrimitiveSink into) {
        if (from instanceof Integer) {
            into.putInt((int) from);
        } else if (from instanceof Long) {
            into.putLong((long) from);
        } else if (from instanceof Short) {
            into.putShort((short) from);
        } else if (from instanceof Byte) {
            into.putByte((byte) from);
        } else if (from instanceof CharSequence) {
            into.putString((CharSequence) from, Charset.defaultCharset());
        } else if (from instanceof Boolean) {
            into.putBoolean((boolean) from);
        } else if (from instanceof Double) {
            into.putDouble((double) from);
        } else if (from instanceof Float) {
            into.putFloat((float) from);
        } else if (from instanceof Character) {
            into.putChar((char) from);
        } else if (from instanceof byte[]) {
            into.putBytes((byte[]) from);
        } else if (from instanceof Collection) {
            Collection list = (Collection) from;
            for (Object each : list) {
                funnel(each, into);
            }
        } else if (from instanceof Map) {
            Map map = (Map) from;
            for (Object each : map.entrySet()) {
                Map.Entry entry = (Map.Entry) each;
                funnel(entry.getKey(), into);
                funnel(entry.getValue(), into);
            }
        } else {
            // TODO - use reflection
            Field[] fields = from.getClass().getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(from);
                    funnel(value, into);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error to read value for field " + field.getName() + " of class " + field.getDeclaringClass().getName());
                }
            }
        }
    }
}
