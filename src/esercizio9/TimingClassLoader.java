package esercizio9;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

public class TimingClassLoader extends ClassLoader{

    private static final String INSERT = "insert";
    private static final String APPEND = "append";

    public void injectTimer() {
        try {
            ClassGen cg = new ClassGen(Repository.lookupClass("java.lang.StringBuilder"));
            Method method  = findAppendOrInsertMethod(cg.getMethods());
            MethodGen mg = new MethodGen(method, cg.getClassName(), cg.getConstantPool());
            InstructionList ilBefore = new InstructionList();
            InstructionList ilAfter = new InstructionList();
            InstructionList il = mg.getInstructionList();
            InstructionHandle ih = il.getStart();
            InstructionFactory factory = new InstructionFactory(cg);

            ilBefore.append(factory.createInvoke("java.lang.System", "currentTimeMillis",Type.LONG,  Type.NO_ARGS, Const.INVOKESTATIC, false));
            int startIndex = mg.addLocalVariable("start", Type.LONG, null, null).getIndex();
            ilBefore.append(factory.createStore(Type.LONG, startIndex));

            ilAfter.append(factory.createGetStatic("java.lang.System", "out", ObjectType.getInstance("java.io.PrintStream")));
            ilAfter.append(factory.createConstant(method.getName() + " elapsed time: " ));
            ilAfter.append(factory.createInvoke("java.io.PrintStream", "print", Type.VOID, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL, false));
            ilAfter.append(factory.createGetStatic("java.lang.System", "out", ObjectType.getInstance("java.io.PrintStream")));
            ilAfter.append(factory.createInvoke("java.lang.System", "currentTimeMillis",Type.LONG,  Type.NO_ARGS, Const.INVOKESTATIC, false));
            ilAfter.append(factory.createLoad(Type.LONG, startIndex));
            ilAfter.append(factory.createBinaryOperation("-", Type.LONG));
            ilAfter.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] {Type.LONG}, Const.INVOKEVIRTUAL, false));

            il.append(ih, ilBefore);
            ih = il.getEnd().getPrev();
            il.append(ih, ilAfter);

            mg.setMaxLocals();
            mg.setMaxStack();
            cg.replaceMethod(method, mg.getMethod());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            TimingClassLoader tcl = new TimingClassLoader();
            tcl.loadClass("java.lang.StringBuilder");
            StringBuilder sb = new StringBuilder("start");
            System.out.println("INSERT");
            sb.insert(5, "!!!");
            System.out.println("APPEND");
            sb.append(new char[] {'a', 'b', 'c', 'd'});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Method findAppendOrInsertMethod(Method[] methods) {
        for(Method m : methods) {
            if(isAppend(m) || isInsert(m)) {
                return m;
            }
        }
        return null;
    }

    private static boolean isInsert(Method m) {
        return INSERT.equals(m.getName()) && m.getArgumentTypes().length == 2
                && Type.INT.equals(m.getArgumentTypes()[0]) && Type.STRING.equals(m.getArgumentTypes()[1]);
    }

    private static boolean isAppend(Method m) {
        return APPEND.equals(m.getName()) && m.getArgumentTypes().length == 1
                && Type.getType(char[].class).equals(m.getArgumentTypes()[0]);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        injectTimer();
        return super.loadClass(name);
    }
}
