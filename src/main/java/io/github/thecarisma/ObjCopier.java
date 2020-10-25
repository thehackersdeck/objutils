package io.github.thecarisma;

import java.lang.reflect.Field;

/**
 * Created by Adewale.Azeez on 10/19/2020
 */
public class ObjCopier {

    public static <T> void copyFields(T target, T source1) throws FatalObjCopierException {
        copyFields(target, false, source1);
    }

    public static <T> void copyFieldsExcept(String[] excepts, T target, T source1) throws FatalObjCopierException {
        copyFieldsExcept(excepts, target, false, source1);
    }

    public static <T> void copySomeFields(String[] selected, T target, T source1) throws FatalObjCopierException {
        copySomeFields(selected, target, false, source1);
    }

    public static <T> void copyTwoObjFields(T target, T source1, T source2) throws FatalObjCopierException {
        copyFields(target, false, source1, source2);
    }

    @SafeVarargs
    public static <T> void copyFields(T target, boolean useValueGreaterThanZero, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(new String[]{}, new String[]{}, target, useValueGreaterThanZero, false, sources);
    }

    @SafeVarargs
    public static <T> void copyFieldsExcept(String[] excepts, T target,
                                            boolean useValueGreaterThanZero, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(excepts, new String[]{}, target, useValueGreaterThanZero, false, sources);
    }

    @SafeVarargs
    public static <T> void copySomeFields(String[] selected, T target,
                                      boolean useValueGreaterThanZero, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(new String[]{}, selected, target, useValueGreaterThanZero, false, sources);
    }

    @SafeVarargs
    public static <T> void copyFieldsWithHigherValue(T target, boolean useHigherValue, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(new String[]{}, new String[]{}, target, true, useHigherValue, sources);
    }

    @SafeVarargs
    public static <T> void copyFieldsWithHigherValueExcept(String[] excepts, T target,
                                                           boolean useHigherValue, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(excepts, new String[]{}, target, true, useHigherValue, sources);
    }

    @SafeVarargs
    public static <T> void copySomeFieldsWithHigherValue(String[] selected, T target,
                                                           boolean useHigherValue, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(new String[]{}, selected, target, true, useHigherValue, sources);
    }

    @SafeVarargs
    public static <T> void copyFieldsWithHigherValue(String[] excepts, String[] selected, T target,
                                                         boolean useHigherValue, T... sources) throws FatalObjCopierException {
        copyFieldsInternal(excepts, selected, target, true, useHigherValue, sources);
    }

    @SafeVarargs
    private static <T> void copyFieldsInternal(String[] excepts, String[] selected, T target, boolean useValueGreaterThanZero,
                                               boolean useHigherValue, T... sources) throws FatalObjCopierException {
        if (sources.length == 0) {
            return;
        }
        Field[] source1Fields = sources[0].getClass().getDeclaredFields();
        for (int index = 0; index < source1Fields.length; ++index) {
            String fieldName = source1Fields[index].getName();
            if (excepts.length > 0) {
                if (existInArray(excepts, fieldName)) {
                    continue;
                }
            }
            if (selected.length > 0) {
                if (!existInArray(selected, fieldName)) {
                    continue;
                }
            }
            try {
                Object[][] fields = new Object[sources.length][2];
                for (int counter = 0; counter < fields.length; ++counter) {
                    Field field = sources[counter].getClass().getDeclaredField(fieldName);
                    fields[counter][0] = field;
                    fields[counter][1] = sources[counter];
                }
                Field targetField = target.getClass().getDeclaredField(fieldName);
                boolean targetFieldIsAccessible = targetField.isAccessible();
                targetField.setAccessible(true);

                Object value = findUsableValue(fields, useValueGreaterThanZero, useHigherValue);
                if (value != null) {
                    targetField.set(target, value);
                }

                targetField.setAccessible(targetFieldIsAccessible);

            } catch (NoSuchFieldException e) {
                //no such field we skip it
                // there is no need to through the exception
                // we trying to get as much data as possible
                // from the two source into the target
            } catch (IllegalAccessException e) {
                throw new FatalObjCopierException("Could not get the value of the property '" + fieldName + "' from the two source");
            }
        }
    }

    private static Object findUsableValue(Object[][] fields, boolean useValueGreaterThanZero, boolean useHigherValue) throws IllegalAccessException {
        Object value = null;
        Object source1 = null;
        Field field1 = null;
        boolean isAccessible1 = false;
        if (fields.length > 0) {
            Object[] objects1 = fields[0];
            if (objects1[0] != null) {
                source1 = objects1[1];
                field1 = ((Field) objects1[0]);
                isAccessible1 = field1.isAccessible();
                field1.setAccessible(true);
                value = field1.get(source1);
            }
            if (fields.length == 1) {
                return value;
            }
        }
        for (int subIndex = 1; subIndex < fields.length; ++subIndex) {
            Object[] objects2 = fields[subIndex];
            Field field2 = null;
            Object source2 = null;
            Object value2 = null;
            boolean isAccessible2 = false;


            if (objects2[0] != null) {
                source2 = objects2[1];
                field2 = ((Field) objects2[0]);
                isAccessible2 = field2.isAccessible();
                field2.setAccessible(true);
                value2 = field2.get(objects2[1]);
            }
            if (field1 == null || value == null) {
                value = value2;

            } else if ((useValueGreaterThanZero || useHigherValue) && value2 != null) {
                if ((ObjUtils.objectsAreSameType(Integer.class, field1.getType(), field2.getType()) ||
                        ObjUtils.objectsAreSameType(int.class, field1.getType(), field2.getType())) &&
                        (((int)value2) > ((int)value))) {
                    value = value2;
                    if (!useHigherValue) {
                        field2.setAccessible(isAccessible2);
                        return value;
                    }

                } else if ((ObjUtils.objectsAreSameType(Long.class, field1.getType(), field2.getType()) ||
                        ObjUtils.objectsAreSameType(long.class, field1.getType(), field2.getType())) &&
                        (((long)value2) > ((long)value))) {
                    value = value2;
                    if (!useHigherValue) {
                        field2.setAccessible(isAccessible2);
                        return value;
                    }

                } else if ((ObjUtils.objectsAreSameType(Float.class, field1.getType(), field2.getType()) ||
                        ObjUtils.objectsAreSameType(float.class, field1.getType(), field2.getType())) &&
                        (((float)value2) > ((float)value))) {
                    value = value2;
                    if (!useHigherValue) {
                        field2.setAccessible(isAccessible2);
                        return value;
                    }

                } else if ((ObjUtils.objectsAreSameType(Double.class, field1.getType(), field2.getType()) ||
                        ObjUtils.objectsAreSameType(double.class, field1.getType(), field2.getType())) &&
                        (((double)value2) > ((double)value))) {
                    value = value2;
                    if (!useHigherValue) {
                        field2.setAccessible(isAccessible2);
                        return value;
                    }

                } else if ((ObjUtils.objectsAreSameType(Boolean.class, field1.getType(), field2.getType()) ||
                        ObjUtils.objectsAreSameType(boolean.class, field1.getType(), field2.getType())) &&
                        (!(boolean)value)) {
                    value = value2;

                }

            }

            if (field2 != null) {
                field2.setAccessible(isAccessible2);
            }
        }
        if (field1 != null) {
            field1.setAccessible(isAccessible1);
        }
        return value;
    }

    private static boolean existInArray(String[] values, String findMe) {
        for (String value : values) {
            if (value.equals(findMe)) {
                return true;
            }
        }
        return false;
    }

}
