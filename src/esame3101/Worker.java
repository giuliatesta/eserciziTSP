package esame3101;

public class Worker implements WorkerI{
    @Override
    public int workerId() {
        return 0;
    }

    @Override
    public double yearSalary() {
        return 0;
    }

    @Override
    public double monthSalary() {
        System.out.println("MONTH SALARY IN WORKER");
        return -1;
    }
}
