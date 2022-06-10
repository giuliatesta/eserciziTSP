package esercizio6;

import java.lang.reflect.Method;
import java.util.Date;

public class TestingFields {
    private Double[] d;
    private Date dd;
    private int the_answer = 42;
    private StateDisplayHandler handler;

    public TestingFields(int n, double val) {
        dd = new Date();
        d = new Double[n];
        for(int i=0; i<n; i++) d[i] = i*val;
        handler = new StateDisplayHandler();
    }

    public void setAnswer(int a) { the_answer = a; }
    public String message() { return "The answer is "+the_answer; }


    public static void main(String[] args) {
        try {
            TestingFields tf = new TestingFields(7, 3.14);
            StateDisplayHandler handler = new StateDisplayHandler();
            Method m = tf.getClass().getMethod("message", null);
            handler.invoke(tf, m, null);
        } catch (Exception e){
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
