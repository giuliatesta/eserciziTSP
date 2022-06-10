package esercizio3;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

public class TestingFields {
    private Double d[];
    private Date dd;
    public static final int i = 42;
    protected static String s = "testing ...";

    public TestingFields(int n, double val) {
        dd = new Date();
        d = new Double[n];
        for(int i=0; i<n; i++) d[i] = i*val;
    }

    public static void main(String[] args) {
        try {
            Class<?> cl = Class.forName("esercizio3.TestingFields");
            Object instance = cl.getConstructor(new Class[]{int.class, double.class}).newInstance(new Object[] {7, 3.14});
            System.out.println(instance.getClass().getName());
            ClassGen cg = new ClassGen(Repository.lookupClass(cl));
            Field fs = Arrays.stream(cg.getFields()).filter(f -> "s".equals(f.getName())).findFirst().get();
            System.out.println(fs.getName());
            FieldGen fg = new FieldGen(fs, cg.getConstantPool());
            InstructionList il = new InstructionList();
            InstructionFactory factory = new InstructionFactory(cg);
            il.append(factory.createGetStatic(cg.getClassName(), "s", Type.STRING));
            il.append(new PUSH(cg.getConstantPool(), "testing...passed!"));
            il.update();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
