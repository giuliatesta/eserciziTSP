package esame3101;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WorkingStudentHandler {

    private WorkerHandler workerHandler;
    private StudentHandler studentHandler;

    public WorkingStudentHandler() {
        workerHandler = new WorkerHandler();
        studentHandler = new StudentHandler();
    }

    public WorkerHandler getWorkerHandler() {
        return workerHandler;
    }

    public StudentHandler getStudentHandler() {
        return studentHandler;
    }

    public static class WorkerHandler extends Worker implements InvocationHandler {
        private WorkerI workerProxy;

        public WorkerHandler() {
            workerProxy = (WorkerI) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {WorkerI.class}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodHandle mh = MethodHandles.lookup()
                    .unreflectSpecial(Worker.class.getMethod(method.getName(),method.getParameterTypes()), getClass());
            Object result = mh.bindTo(this).invokeWithArguments(args);
            return result;
        }

        @Override
        public int workerId() {
            return workerProxy.workerId();
        }

        @Override
        public double yearSalary() {
            return workerProxy.yearSalary();
        }

        @Override
        public double monthSalary() {
            return workerProxy.monthSalary();
        }
    }

    public static class StudentHandler extends Student implements InvocationHandler {

        private StudentI studentProxy;

        public StudentHandler() {
            studentProxy = (StudentI) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {StudentI.class}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodHandle mh = MethodHandles.lookup()
                    .unreflectSpecial(Student.class.getMethod(method.getName(), method.getParameterTypes()), getClass());
            Object result = mh.bindTo(this).invokeWithArguments(args);
            return result;
        }

        @Override
        public String[] curricula() {
            return studentProxy.curricula();
        }

        @Override
        public int studentId() {
            return studentProxy.studentId();
        }

        @Override
        public double average() {
            return studentProxy.average();
        }
    }
}
