package dummys;
import common.Student;
import common.studentQuestion;

import java.rmi.RemoteException;

public class SimpleStudent implements Student {
    public int universityId;
    public studentQuestion recievedQuestion;
    public double grade;
    public SimpleStudent(int universityId){
        this.universityId=universityId;
    }

    public void sendQuestion(studentQuestion givenQuestion) throws RemoteException{
        this.recievedQuestion=givenQuestion;
    }
    public void finishExam(double grade) throws RemoteException{
        this.grade=grade;
    }
}
