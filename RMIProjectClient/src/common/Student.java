package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Student extends Remote {
    void sendQuestion(Question givenQuestion) throws RemoteException;
    void finishExam(double grade) throws RemoteException;
}
