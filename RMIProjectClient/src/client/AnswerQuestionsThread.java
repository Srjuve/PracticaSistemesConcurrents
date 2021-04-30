package client;

import common.Question;
import common.Room;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class AnswerQuestionsThread extends Thread{
    Room actualRoom;
    StudentImplementation student;
    boolean cancel = false;
    Thread thisThread = null;
    Semaphore sem;
    public AnswerQuestionsThread(Room actualRoom, StudentImplementation student, Semaphore sem){
        this.actualRoom = actualRoom;
        this.student = student;
        thisThread=Thread.currentThread();
        this.sem=sem;
    }

    public void cancel(){
        cancel = true;
        this.interrupt();
    }
    @Override
    public void run(){
            while(true && !cancel && !thisThread.isInterrupted()){
                try {
                    System.out.println("Patatatatatatata");
                    this.sem.acquire();
                }catch (InterruptedException ex){
                    return;
                }
            }
            return;
    }

    private String getAnswers(Question question){
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

    public static void printAnswers(List<String> answers){
        for(int i=0;i<answers.size();i+=1){
            System.out.println(String.valueOf(i+1)+":"+answers.get(i));
        }
    }
}
