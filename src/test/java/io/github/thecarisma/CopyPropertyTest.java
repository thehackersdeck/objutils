package io.github.thecarisma;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Adewale.Azeez on 11/25/2020
 */
public class CopyPropertyTest {

    static class TestIgnoreClass {
        @CopyProperty(ignore = true)
        long id;
        String name;
        @CopyProperty(ignore = true)
        String permanent;
        String description;
    }

    TestIgnoreClass testIgnoreClass1;
    TestIgnoreClass testIgnoreClass2;
    TestIgnoreClass testIgnoreClass3;

    @Before
    public void setup() {
        testIgnoreClass1 = new TestIgnoreClass();
        testIgnoreClass1.id = 2;
        testIgnoreClass1.name = "Thecarisma";
        testIgnoreClass1.permanent = "Glue";
        testIgnoreClass1.description = "test ignore annotation class 1";

        testIgnoreClass2 = new TestIgnoreClass();
        testIgnoreClass2.id = 4;
        testIgnoreClass2.name = "hackers Deck";
        testIgnoreClass2.permanent = "Marker";
        testIgnoreClass2.description = "test ignore annotation class 2";

        testIgnoreClass3 = new TestIgnoreClass();
        testIgnoreClass3.id = 6;
        testIgnoreClass3.name = "Quick Utils";
        testIgnoreClass3.permanent = "Adhesive";
        testIgnoreClass3.description = "test ignore annotation class 3";
    }

    @Test
    public void testIgnoreDuringCopy() throws FatalObjCopierException {
        ObjCopier.copyFields(testIgnoreClass1, testIgnoreClass2);

        Assert.assertNotEquals(testIgnoreClass1.id, testIgnoreClass2.id);
        Assert.assertEquals(testIgnoreClass1.name, testIgnoreClass2.name);
        Assert.assertNotEquals(testIgnoreClass1.permanent, testIgnoreClass2.permanent);
        Assert.assertEquals(testIgnoreClass1.description, testIgnoreClass2.description);
        Assert.assertFalse(ObjUtils.deepCompare(testIgnoreClass1, testIgnoreClass2));
    }

}
