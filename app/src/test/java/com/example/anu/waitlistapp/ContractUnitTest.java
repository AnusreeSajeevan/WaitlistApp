package com.example.anu.waitlistapp;

import android.provider.BaseColumns;

import com.example.anu.waitlistapp.data.WaitlistContract;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * Created by Design on 07-12-2017.
 */

public class ContractUnitTest {

    @Test
    public void inner_class_exist()throws Exception
    {
        Class[] innerClasses = WaitlistContract.class.getDeclaredClasses();
        assertEquals("There should be atleast 1 inner class inside contract class", 1, innerClasses.length);
    }

    @Test
    public void inner_class_type_correct()throws Exception{
        Class[] innerClasses = WaitlistContract.class.getDeclaredClasses();
        assertEquals("There should be atleast 1 inner class inside contract class", 1, innerClasses.length);

        Class innerClass = innerClasses[0];
        assertTrue("inner class should implement BaseColumn interface", BaseColumns.class.isAssignableFrom(innerClass));
        assertTrue("inner class should be final", Modifier.isFinal(innerClass.getModifiers()));
        assertTrue("inner class should be static", Modifier.isStatic(innerClass.getModifiers()));
    }

    @Test
    public void inner_class_memberscorrect()throws Exception{
        Class[] innerClasses = WaitlistContract.class.getDeclaredClasses();
        assertEquals("There should be atleast 1 inner class inside contract class", 1, innerClasses.length);

        Class innerClass = innerClasses[0];
        Field[] fields = innerClass.getFields();
        for (Field field : fields) {
            assertTrue("inner class members should be of String type", field.getType()==String.class);
            assertTrue("inner class members should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("inner class members should be static", Modifier.isStatic(field.getModifiers()));
        }
    }
}
