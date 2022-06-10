package esame2402;

public class TestB {
    private String test_d;
    private char test_g;
    protected final boolean test_b = true;
    public int test_c;
    public static boolean test_e;
    public static final int test_a= 7;
    static final String test_f = "Hello";

    public char gettest_g() {
        return test_g;
    }

    public int gettest_c() {
        return test_c;
    }

    public static boolean gettest_e() {
        return test_e;
    }

    public void settest_d(String test_d) {
        this.test_d = test_d;
    }

    public void settest_g(char test_g) {
        this.test_g = test_g;
    }

    public void settest_c(int test_c) {
        this.test_c = test_c;
    }

    public static void settest_e(boolean test_e) {
        TestB.test_e = test_e;
    }

    public TestB() {}
    public TestB(int a2, String a3, char a4){}

    public static void setStatic(boolean a){}
}
