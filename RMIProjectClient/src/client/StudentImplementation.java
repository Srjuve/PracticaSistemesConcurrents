package client;

import common.Question;
import common.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class StudentImplementation  extends UnicastRemoteObject implements Student{
    private Integer universityId;
    private Question actualQuestion;
    private boolean finished=false;
    private Semaphore semaphore;
    private double totalGrade;

    public StudentImplementation(Integer universityId,Semaphore semaphore) throws RemoteException{
        super();
        this.universityId=universityId;
        this.semaphore =semaphore;
    }

    public boolean getExamState(){
        return finished;
    }

    public void setExamState(){
        this.finished=true;
    }
    public Question getActualQuestion(){
        return this.actualQuestion;
    }

    public Integer getUniversityId(){
        return this.universityId;
    }
    public double getGrade(){
        return this.totalGrade;
    }
    public void sendQuestion(Question givenQuestion) throws RemoteException{
        this.actualQuestion=givenQuestion;
        this.semaphore.release();
        //Avisar al Client que hi ha una pregunta disponible
    }
    public void finishExam(double grade) throws RemoteException{
        totalGrade = grade;
        finished=true;
        this.semaphore.release();
        //Avisar al Client que s'ha finalitzat el examen(I finalitzar-li)
    }
}
