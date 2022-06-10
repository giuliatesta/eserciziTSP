package esercizio7;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NestedCallsHandler extends NestedCalls implements InvocationHandler {
    private NestedCallsI proxy;
    private int counter;

    public NestedCallsHandler() {
        proxy = (NestedCallsI) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{NestedCallsI.class}, this);
        counter =0;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        counter++;
        System.out.println(repeat('!', counter));
        MethodHandle mh = MethodHandles.lookup()
                .unreflectSpecial(NestedCalls.class.getMethod(method.getName(), method.getParameterTypes()), getClass());
        Object result =mh.bindTo(this).invokeWithArguments(args);
        counter--;
        return result;
    }

    private String repeat(char c, int counter) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<counter; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public int a() {
        return proxy.a();
    }

    @Override
    public int b(int a) {
        return proxy.b(a);
    }

    @Override
    public int c(int a) {
        return proxy.c(a);
    }

    public static void main(String[] args) {
        NestedCallsHandler nch = new NestedCallsHandler();
        System.out.println("A");
        nch.a();
        System.out.println("B");
        nch.b(nch.a());
        System.out.println("C");
        nch.c(nch.a());
    }
}
