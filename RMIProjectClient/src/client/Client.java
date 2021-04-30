package client;
import common.Question;
import common.Room;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client {

    private static void waitForFinishSignal(StudentImplementation student,Object finishLock,AnswerQuestionsThread answerQuestion){
        try {
            answerQuestion.start();
            System.out.println("Started thread");
            synchronized (finishLock) {
                System.out.println("Waiting");
                finishLock.wait();
                System.out.println("Finished waiting");
            }
            if(answerQuestion.isAlive()){
                answerQuestion.cancel();
            }
            System.out.println("You have finished the exam");
            System.out.println("Your grade is: "+String.valueOf(student.getGrade()));
        }catch (Exception ex){
            System.exit(-1);
        }
    }
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Semaphore semaphore = new Semaphore(0);
            Integer id=new Integer(321);
            Object finishLock = new Object();
            StudentImplementation student= new StudentImplementation(id,semaphore,finishLock);
            Room stub = (Room) registry.lookup("room");
            stub.joinExam(id,student);
            AnswerQuestionsThread answerQuestion = new AnswerQuestionsThread(stub,student, semaphore,finishLock);
            waitForFinishSignal(student,finishLock,answerQuestion);
            System.exit(0);
        }catch(exceptions.notAcceptingStudentsException ex){
            System.out.println("The Room is not accepting new Students.");
            System.exit(0);
        }catch(exceptions.studentAlreadyJoinedException ex){
            System.out.println("You already joined to this exam.");
            System.exit(0);
        }catch (Exception e) {
            System.out.println("Internal Error");
            System.exit(0);
        }
    }
}
