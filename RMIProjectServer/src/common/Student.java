package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Student extends Remote {
    void sendQuestion(studentQuestion givenQuestion) throws RemoteException;
    void finishExam(double grade) throws RemoteException;

}
