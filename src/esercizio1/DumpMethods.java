package esercizio1;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;

public class DumpMethods {

    // Reflection
    /*
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("ERRORE");
            } else {
                String className = args[0];
                Class<?> cl = Class.forName(className);
                Method[] methods = cl.getDeclaredMethods();
                Field[] fields = cl.getDeclaredFields();
                System.out.println("Class Name: " + cl.getName());
                System.out.println("METHODS:");
                for(Method m : methods) {
                    System.out.println(m.getReturnType() + " "+ m.getName() + "()");
                }

                System.out.println("FIELDS");
                for(Field f : fields) {
                    System.out.println(f.getType() + " " + f.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    } */

    //BCEL
    /*
    public static void main(String[] args) {
        try {
            String className = args[0];
            JavaClass jc = Repository.lookupClass(className);
            ClassGen cg = new ClassGen(jc);
            System.out.println("Class Name: " + cg.getClassName());
            Method[] methods = cg.getMethods();
            Field[] fields = cg.getFields();
            System.out.println("METHODS:");
            for(Method m: methods) {
                System.out.println(m.getReturnType() + " " + m.getName() + "()");
            }

            System.out.println("FIELDS:");
            for(Field f: fields) {
                System.out.println(f.getType() + " " + f.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    //Javassist
    public static void main(String[] args) {
        try {
            String className = args[0];
            CtClass cl = ClassPool.getDefault().get(className);
            System.out.println("Class Name: " + cl.getName());
            CtMethod[] methods = cl.getDeclaredMethods();
            CtField[] fields = cl.getDeclaredFields();
            System.out.println("METHODS:");
            for(CtMethod m: methods) {
                System.out.println(m.getReturnType() + " " + m.getName() + "()");
            }

            System.out.println("FIELDS:");
            for(CtField f: fields) {
                System.out.println(f.getType() + " " + f.getName());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
