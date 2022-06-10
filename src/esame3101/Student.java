package esame3101;

public class Student implements StudentI{
    @Override
    public String[] curricula() {
        return new String[0];
    }

    @Override
    public int studentId() {
        return 0;
    }

    @Override
    public double average() {
        return 30;
    }
}
