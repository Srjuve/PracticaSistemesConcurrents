package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Room extends Remote {
    void joinExam(Integer universityId, Student joinedStudent) throws RemoteException,exceptions.notAcceptingStudentsException,exceptions.studentAlreadyJoinedException,exceptions.examErrorException;
    void sendAnswer(Integer universityID, String answer) throws RemoteException;
}
