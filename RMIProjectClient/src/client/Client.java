package client;
import common.Room;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client {

    private static void waitForFinishSignal(StudentImplementation student,Object finishLock,AnswerQuestionsThread answerQuestion){
        //We will wait until the signal to finish is recieved and then we will show the grades of the actual student and finish the execution
        try {
            answerQuestion.start();
            synchronized (finishLock) {
                finishLock.wait();
            }
            System.out.println("You have finished the exam");
            System.out.println("Your grade is: "+String.valueOf(student.getGrade()));
        }catch (Exception ex){
            System.exit(-1);
        }
    }

    private static int getStudentId(){
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter the your id");
        int userid=0;
        boolean valid=false;
        while(!valid) {
            try {
                userid = Integer.parseInt(scan.nextLine());
                valid=true;
            } catch (Exception e) {
                System.out.println("You didn't enter a valid Id, try again.");
            }
        }
        return userid;
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Semaphore semaphore = new Semaphore(0);
            Integer id=new Integer(getStudentId());
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
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
