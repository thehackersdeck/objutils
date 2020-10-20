package io.github.thecarisma;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adewale.Azeez on 10/19/2020
 */
public class ObjCopierTest {

    static class AnObject {
        long id;
        String name;
        String email;
        List<String> socialProfiles;
        boolean banned;
        int deleted;
    }

    AnObject obj1;
    AnObject obj2;
    AnObject obj3;
    AnObject obj4;
    AnObject obj5;
    AnObject obj6;
    AnObject obj7;

    @Before
    public void Setup() {
        obj1 = new AnObject();
        obj2 = new AnObject();
        obj3 = new AnObject();
        obj4 = new AnObject();
        obj5 = new AnObject();
        obj6 = new AnObject();
        obj7 = new AnObject();

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
    }

    @Test
    public void TestShallowCompare1() {
        Assert.assertTrue(ObjCopier.shallowCompare(obj1, obj1));
        Assert.assertFalse(ObjCopier.shallowCompare(obj1, obj2));
        Assert.assertTrue(ObjCopier.shallowCompare(obj2, obj2));
    }

    @Test
    public void TestDeepCompare1() throws FatalObjCopierException {
        Assert.assertFalse(ObjCopier.deepCompare(obj1, obj2));
        Assert.assertFalse(ObjCopier.deepCompare(obj1, obj3));
        Assert.assertFalse(ObjCopier.deepCompare(obj2, obj3));
        Assert.assertFalse(ObjCopier.deepCompare(obj5, obj3));
        Assert.assertFalse(ObjCopier.deepCompare(obj2, obj5));
    }

    @Test
    public void TestCopy1() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyFields(obj, obj2);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj2));
        Assert.assertFalse(ObjCopier.deepCompare(obj, obj6));
    }

    @Test
    public void TestCopy2() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyTwoObjFields(obj, obj2, obj1);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj6));
    }

    @Test
    public void TestCopy3() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyFields(obj, true, obj2, obj1);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj7));
    }

    @Test
    public void TestCopy4() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyFields(obj, true, obj2, obj1, obj5);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj7));
    }

    @Test
    public void TestCopy5() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyFieldsWithHigherValue(obj, true, obj1, obj2, obj3, obj5);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj4));
    }

    @Test
    public void TestCopyExcept1() throws FatalObjCopierException {
        AnObject obj = new AnObject();
        ObjCopier.copyFieldsWithHigherValueExcept(new String[] {}, obj, true, obj1, obj2, obj3, obj5);
        Assert.assertTrue(ObjCopier.deepCompare(obj, obj4));

        obj = new AnObject();
        ObjCopier.copyFieldsWithHigherValueExcept(new String[] {"id"}, obj, true, obj1, obj2, obj3, obj5);
        obj.id = 0;
        Assert.assertFalse(ObjCopier.deepCompare(obj, obj4));
    }

}
