package dummys;
import common.Question;
import common.Student;

import java.rmi.RemoteException;

public class SimpleStudent implements Student {
    public int universityId;
    public Question recievedQuestion;
    public double grade;
    public SimpleStudent(int universityId){
        this.universityId=universityId;
    }

    public void sendQuestion(Question givenQuestion) throws RemoteException{
        this.recievedQuestion=givenQuestion;
    }
    public void finishExam(double grade) throws RemoteException{
        this.grade=grade;
    }
}
