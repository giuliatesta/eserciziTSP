package esame2402;

public class TestA {

    public final int test_const1 = 7;
    public final static float test_const2 = 3.0f;
    private final String test_const3 = "Hello World!";
    public int test_attr1;
    private int test_attr2;
    private float test_attr3;
    private String test_attr4;
    private static int test_attr5;
    private static String test_attr6;

    public float gettest_attr2() {
        return test_attr2;
    }
    public float gettest_attr3() {
        return test_attr3;
    }
    public void settest_attr3(int test_attr3) {
        this.test_attr3 = test_attr3;
    }
    public String gettest_attr4() {
        return test_attr4;
    }
    public void settest_attr4(String test_attr4) {
        this.test_attr4 = test_attr4;
    }
    public static int gettest_attr5() {
        return test_attr5;
    }
    public static void settest_attr5(int test_attr5) {
        TestA.test_attr5 = test_attr5;
    }
    public String gettest_attr6() {
        return test_attr6;
    }
    public static void settest_attr6(String test_attr6) {
        TestA.test_attr6 = test_attr6;
    }

    public TestA(int a1, int a2, float a3, String a4, int a5, String a6) {}
    public TestA(float a3, String a4, int a1, int a2) {}

    public static void setStatic(String a) {}
}
