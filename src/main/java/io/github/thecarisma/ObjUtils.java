package io.github.thecarisma;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Adewale.Azeez on 10/30/2020
 */
public class ObjUtils {

    public static <T> Field[] getFields(T source) {
        return source.getClass().getDeclaredFields();
    }

    public static <T> Method getObjMethod(T source, String functionName, Class<?>... parameterType) throws NoSuchMethodException {
        return source.getClass().getMethod(functionName, parameterType);
    }

    public static <T> Object invokeObjMethod(T source, String functionName, Class<?>[] parameterType, Object... parameters)
            throws FatalObjCopierException {
        try {
            Method method = getObjMethod(source, functionName, parameterType);
            return method.invoke(source, parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new FatalObjCopierException(e.getMessage());
        }
    }

    public static void mapObjFields() {

    }

    public static  <T> boolean shallowCompare(T source1, T source2) {
        return source1.equals(source2);
    }

    public static <T> boolean deepCompare(T source1, T source2) throws FatalObjCopierException {
        Field[] source1Fields = source1.getClass().getDeclaredFields();
        Field[] source2Fields = source2.getClass().getDeclaredFields();

        if (source1Fields.length != source2Fields.length) {
            return false;
        }
        for (int index = 0; index < source1Fields.length; ++index) {
            Field field = source1Fields[index];
            Field field2 = source2Fields[index];
            boolean accessible = field.isAccessible();
            boolean accessible2 = field2.isAccessible();
            field.setAccessible(true);
            field2.setAccessible(true);

            Object value1 = null;
            Object value2 = null;
            try {
                value1 = field.get(source1);
                value2 = field2.get(source2);
            } catch (IllegalAccessException e) {
                throw new FatalObjCopierException("Could not get the value of the property '" + field.getName() + "' from the two source for compare");
            }
            if ((value1 == null && value2 != null) || (value1 != null && !value1.equals(value2))) {
                return false;
            }

            field.setAccessible(accessible);
            field2.setAccessible(accessible2);
        }
        return true;
    }

    public static boolean objectsAreSameType(Class<?> objectTypeCheck, Class<?>... objectTypes) {
        for (Class<?> objectType : objectTypes) {
            if (objectType != objectTypeCheck) {
                return false;
            }
        }
        return true;
    }

}
