package esame3101;

import esame3101.WorkingStudentHandler.StudentHandler;
import esame3101.WorkingStudentHandler.WorkerHandler;

public class WorkingStudent {

    private WorkingStudentHandler handler;

    public WorkingStudent() {
        handler = new WorkingStudentHandler();
    }

    public static void main(String[] args) {
        WorkingStudent ws = new WorkingStudent();
        System.out.println("The average of the Working Student is " + ws.handler.getStudentHandler().average());
        System.out.println("The monthly salary of the Working Student is " + ws.handler.getWorkerHandler().monthSalary());
    }
}
