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


    public static void printAnswers(List<String> answers){
        for(int i=0;i<answers.size();i+=1){
            System.out.println(String.valueOf(i+1)+":"+answers.get(i));
        }
    }

    public static String getAnswers(Question question){
        int answer=-1;
        Scanner scan = new Scanner(System.in);
        List<String> answers=question.getAnswers();
        printAnswers(answers);
        boolean correctValue=false;
        while (!correctValue) {
            try {
                answer = scan.nextInt();
                if(answer>0 && answer<=answers.size()) {
                    correctValue = true;
                }else{
                    System.out.println("Invalid value, try again");
                }
            }catch (Exception ex){
                System.out.println("Invalid value, try again");
            }
        }
        return answers.get(answer-1);
    }

    public static void startExam(Room examRoom, StudentImplementation actualStudent,Semaphore semaphore) throws InterruptedException {
        try {
            Integer id = actualStudent.getUniversityId();
            while (!actualStudent.getExamState()) {
                System.out.println("About to wait");
                semaphore.acquire();
                if (!actualStudent.getExamState()) {
                    Question recievedQuestion = actualStudent.getActualQuestion();
                    System.out.println(recievedQuestion.getQuestion());
                    String answer = getAnswers(recievedQuestion);
                    examRoom.sendAnswer(id, answer);
                }
            }
            System.out.println("Exam finished");
            System.out.println("Your Grade is: "+actualStudent.getGrade());
        }catch (RemoteException ex){
            if(actualStudent.getExamState()){
                System.out.println("Exam finished");
                System.out.println("Your Grade is: "+actualStudent.getGrade());
            }else {
                System.out.println("Room cannot be reached your grade will be set using your actual answers");
            }
        }
    }


    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Semaphore semaphore = new Semaphore(0);
            Integer id=new Integer(321);
            StudentImplementation student= new StudentImplementation(id,semaphore);
            Room stub = (Room) registry.lookup("room");
            stub.joinExam(id,student);
            AnswerQuestionsThread answerQuestion = new AnswerQuestionsThread(stub,student, new Semaphore(0));
            answerQuestion.run();
            answerQuestion.cancel();
            //startExam(stub,student,semaphore);
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
