package itHillel.testRunner;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {
    int priority() default 5;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface BeforeSuite {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface AfterSuite {}

public class TestRunner {

    public static void start(Class<?> testClass) throws Exception {
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> beforeSuiteMethods = new ArrayList<>();
        List<Method> afterSuiteMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                afterSuiteMethods.add(method);
            }
        }
        if (beforeSuiteMethods.size() > 1 || afterSuiteMethods.size() > 1) {
            throw new RuntimeException("There should be only one method with @BeforeSuite and @AfterSuite annotations in a test class");
        }
        beforeSuiteMethods.forEach(method -> invokeMethod(method, null));
        Collections.sort(testMethods, Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).priority()).reversed());
        testMethods.forEach(method -> invokeMethod(method, null));
        afterSuiteMethods.forEach(method -> invokeMethod(method, null));
    }

    private static void invokeMethod(Method method, Object instance) {
        try {
            method.setAccessible(true);
            method.invoke(instance);
            System.out.println("Executed method: " + method.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
