package server;

import common.Room;
import common.Student;

import java.rmi.RemoteException;

public class RoomImplementation implements Room {

    public void joinExam(int universityId, Student joinedStudent) throws RemoteException{
        System.out.println("Patata");
    }
    public void SendAnswer(int universityID,String answer) throws RemoteException{
        System.out.println("Patata2");
    }
}
