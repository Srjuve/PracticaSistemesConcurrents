package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Room extends Remote {
    void joinExam(int universityId, Student joinedStudent) throws RemoteException;
    void SendAnswer(int universityID,String answer) throws RemoteException;
}
