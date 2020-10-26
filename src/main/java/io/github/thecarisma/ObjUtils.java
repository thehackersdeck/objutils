package io.github.thecarisma;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Adewale.Azeez on 10/30/2020
 */
public class ObjUtils {

    public static <T> Field[] getFields(T object) {
        return object.getClass().getDeclaredFields();
    }

    public static <T> Method getObjMethod(T object, String functionName, Class<?>... parameterType) throws NoSuchMethodException {
        return object.getClass().getMethod(functionName, parameterType);
    }

    public static <T> Object invokeObjMethod(T object, String functionName, Class<?>[] parameterType, Object... parameters)
            throws FatalObjCopierException {
        try {
            Method method = getObjMethod(object, functionName, parameterType);
            return method.invoke(object, parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new FatalObjCopierException(e.getMessage());
        }
    }

    public static void mapObjFields() {

    }

    public static  <T> boolean shallowCompare(T object1, T object2) {
        return object1.equals(object2);
    }

    public static <T> boolean deepCompare(T object1, T object2) throws FatalObjCopierException {
        Field[] object1Fields = object1.getClass().getDeclaredFields();
        Field[] object2Fields = object2.getClass().getDeclaredFields();

        if (object1Fields.length != object2Fields.length) {
            return false;
        }
        for (int index = 0; index < object1Fields.length; ++index) {
            Field field = object1Fields[index];
            Field field2 = object2Fields[index];
            boolean accessible = field.isAccessible();
            boolean accessible2 = field2.isAccessible();
            field.setAccessible(true);
            field2.setAccessible(true);

            Object value1 = null;
            Object value2 = null;
            try {
                value1 = field.get(object1);
                value2 = field2.get(object2);
            } catch (IllegalAccessException e) {
                throw new FatalObjCopierException("Could not get the value of the property '" + field.getName() + "' from the two object for compare");
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
