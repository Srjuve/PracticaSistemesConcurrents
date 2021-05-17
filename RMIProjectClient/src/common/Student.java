package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Student extends Remote {
    void sendQuestion(studentQuestion givenStudentQuestion) throws RemoteException;
    void finishExam(double grade) throws RemoteException;
}
