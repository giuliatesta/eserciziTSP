package esercizio6;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StateDisplayHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("BEFORE INVOCATION: " + proxy);
        Object result = method.invoke(proxy, args);
        System.out.println("AFTER INVOCATION: " + proxy + "\tRESULT: " + result);
        return result;
    }
}
