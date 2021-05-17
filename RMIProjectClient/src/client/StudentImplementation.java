package client;

import common.studentQuestion;
import common.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class StudentImplementation  extends UnicastRemoteObject implements Student{
    private Integer universityId;
    private studentQuestion actualStudentQuestion;
    private boolean finished=false;
    private Semaphore semaphore;
    private double totalGrade;
    private Object finishLock;

    public StudentImplementation(Integer universityId,Semaphore semaphore,Object finishLock) throws RemoteException{
        super();
        this.universityId=universityId;
        this.semaphore =semaphore;
        this.finishLock=finishLock;
    }

    public boolean getExamState(){
        return finished;
    }

    public studentQuestion getActualQuestion(){
        return this.actualStudentQuestion;
    }

    public Integer getUniversityId(){
        return this.universityId;
    }

    public double getGrade(){
        return this.totalGrade;
    }

    public void sendQuestion(studentQuestion givenStudentQuestion) throws RemoteException{
        //Inform the Student that the requested question is ready to be answered
        this.actualStudentQuestion = givenStudentQuestion;
        this.semaphore.release();
    }
    public void finishExam(double grade) throws RemoteException{
        //Inform the Student that the exam has ended
        totalGrade = grade;
        finished=true;
        synchronized (finishLock){
            finishLock.notify();
        }
    }
}
