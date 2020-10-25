package io.github.thecarisma;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adewale.Azeez on 10/30/2020
 */
public class ObjUtilsTest {

    static class ObjectWithMethods {
        String name;
        String email;

        public ObjectWithMethods(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    ObjCopierTest.AnObject obj1;
    ObjCopierTest.AnObject obj2;
    ObjCopierTest.AnObject obj3;
    ObjCopierTest.AnObject obj4;
    ObjCopierTest.AnObject obj5;
    ObjCopierTest.AnObject obj6;
    ObjCopierTest.AnObject obj7;
    ObjectWithMethods objectWithMethods;

    @Before
    public void Setup() {
        obj1 = new ObjCopierTest.AnObject();
        obj2 = new ObjCopierTest.AnObject();
        obj3 = new ObjCopierTest.AnObject();
        obj4 = new ObjCopierTest.AnObject();
        obj5 = new ObjCopierTest.AnObject();
        obj6 = new ObjCopierTest.AnObject();
        obj7 = new ObjCopierTest.AnObject();

        obj1.id = 1;
        obj1.banned = true;
        obj1.name = "Thecarisma";

        obj5.id = 20;
        obj5.deleted = 1;

        obj2.email = "whatisthis@gmail.com";
        obj2.socialProfiles = new ArrayList<String>();
        obj2.deleted = 1;
        obj2.socialProfiles.add("https://github.com/Thecarisma");
        obj2.socialProfiles.add("https://github.com/Thecarisma");

        obj3.id = 2;
        obj3.banned = false;
        obj3.socialProfiles = new ArrayList<String>();
        obj3.socialProfiles.add("https://dev.to/iamthecarisma");
        obj3.socialProfiles.add("https://twitter.com/iamthecarisma");
        obj3.deleted = 7;

        obj6.id = 0;
        obj6.banned = false;
        obj6.name = "Thecarisma";
        obj6.email = "whatisthis@gmail.com";
        obj6.socialProfiles = new ArrayList<String>();
        obj6.deleted = 1;
        obj6.socialProfiles.add("https://github.com/Thecarisma");
        obj6.socialProfiles.add("https://github.com/Thecarisma");

        obj7.id = 1;
        obj7.banned = true;
        obj7.name = "Thecarisma";
        obj7.email = "whatisthis@gmail.com";
        obj7.socialProfiles = new ArrayList<String>();
        obj7.deleted = 1;
        obj7.socialProfiles.add("https://github.com/Thecarisma");
        obj7.socialProfiles.add("https://github.com/Thecarisma");

        obj4.id = 20;
        obj4.banned = true;
        obj4.name = "Thecarisma";
        obj4.email = "whatisthis@gmail.com";
        obj4.socialProfiles = new ArrayList<String>();
        obj4.deleted = 7;
        obj4.socialProfiles.add("https://github.com/Thecarisma");
        obj4.socialProfiles.add("https://github.com/Thecarisma");

        objectWithMethods = new ObjectWithMethods("thecarisma", "whatdoyoucare@novalidity.com");
    }

    @Test
    public void testGetFields() {
        Assert.assertEquals(ObjUtils.getFields(objectWithMethods).length, 2);
        Assert.assertEquals(ObjUtils.getFields(objectWithMethods)[0].getName(), "name");
        Assert.assertEquals(ObjUtils.getFields(objectWithMethods)[1].getName(), "email");
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Assert.assertEquals(objectWithMethods.getName(), "thecarisma");
        Method method = ObjUtils.getObjMethod(objectWithMethods, "setName", String.class);
        method.invoke(objectWithMethods, "amsiraceht");
        Assert.assertEquals(objectWithMethods.getName(), "amsiraceht");

        Assert.assertNotNull(objectWithMethods.getEmail());
        method = ObjUtils.getObjMethod(objectWithMethods, "setEmail", String.class);
        method.invoke(objectWithMethods, new Object[]{null});
        Assert.assertNull(objectWithMethods.getEmail());
    }

    @Test
    public void testInvokeObjMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, FatalObjCopierException {
        Assert.assertEquals(objectWithMethods.getName(), "thecarisma");
        ObjUtils.invokeObjMethod(objectWithMethods, "setName",
                new Class<?>[]{String.class}, "amsiraceht");
        Assert.assertEquals(objectWithMethods.getName(), "amsiraceht");

        Assert.assertNotNull(objectWithMethods.getEmail());
        ObjUtils.invokeObjMethod(objectWithMethods, "setEmail",
                new Class<?>[]{String.class}, new Object[]{null});
        Assert.assertNull(objectWithMethods.getEmail());


        Assert.assertNull(ObjUtils.invokeObjMethod(objectWithMethods, "getEmail", null));
        Assert.assertEquals(ObjUtils.invokeObjMethod(objectWithMethods, "getName", null),
                "amsiraceht");
    }

    @Test
    public void TestShallowCompare1() {
        Assert.assertTrue(ObjUtils.shallowCompare(obj1, obj1));
        Assert.assertFalse(ObjUtils.shallowCompare(obj1, obj2));
        Assert.assertTrue(ObjUtils.shallowCompare(obj2, obj2));
    }

    @Test
    public void TestDeepCompare1() throws FatalObjCopierException {
        Assert.assertFalse(ObjUtils.deepCompare(obj1, obj2));
        Assert.assertFalse(ObjUtils.deepCompare(obj1, obj3));
        Assert.assertFalse(ObjUtils.deepCompare(obj2, obj3));
        Assert.assertFalse(ObjUtils.deepCompare(obj5, obj3));
        Assert.assertFalse(ObjUtils.deepCompare(obj2, obj5));
    }

}
