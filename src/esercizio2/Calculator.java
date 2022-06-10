package esercizio2;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.MethodGen;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
    public int mul(int a, int b) {
        return a * b;
    }
    public double div(double a, double b) {
        return a / b;
    }
    public double neg(double a) {
        return -a;
    }


    //BCEL
    public static void main(String[] args) {
        // ClassName methodName numberOfArgs Args[]
        try {
            ClassGen cg = new ClassGen(Repository.lookupClass(args[0]));
            MethodGen mg;
            for(Method m : cg.getMethods()){
                if(m.getName().equalsIgnoreCase(args[1]) && m.getArgumentTypes().length == Integer.parseInt(args[2])) {
                    mg = new MethodGen(m, cg.getClassName(), cg.getConstantPool());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
