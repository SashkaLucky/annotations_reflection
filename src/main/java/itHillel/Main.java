package itHillel;

import itHillel.testRunner.MyTestClass;
import itHillel.testRunner.TestRunner;

public class Main {
    public static void main(String[] args) {
        try {
            TestRunner.start(MyTestClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}