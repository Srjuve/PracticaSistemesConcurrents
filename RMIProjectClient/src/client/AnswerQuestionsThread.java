package client;

import common.studentQuestion;
import common.Room;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class AnswerQuestionsThread extends Thread{
    Room actualRoom;
    StudentImplementation student;
    boolean cancel = false;
    Thread thisThread = null;
    Semaphore sem;
    Object finishLock;

    public AnswerQuestionsThread(Room actualRoom, StudentImplementation student, Semaphore sem,Object finishLock){
        this.actualRoom = actualRoom;
        this.student = student;
        thisThread=Thread.currentThread();
        this.sem=sem;
        this.finishLock=finishLock;
    }

    @Override
    public void run(){
        Integer id = this.student.getUniversityId();
        try {
            while (!this.student.getExamState() && !cancel && !thisThread.isInterrupted()) {
                System.out.println("Waiting for question");
                try {
                    this.sem.acquire();
                    if (!this.student.getExamState()) {
                        studentQuestion recievedStudentQuestion = this.student.getActualQuestion();
                        System.out.println(recievedStudentQuestion.getQuestion());
                        String answer = getAnswers(recievedStudentQuestion);
                        if(!this.student.getExamState()) {
                            this.actualRoom.sendAnswer(id, answer);
                        }
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Exam finished");
                }
            }
        }catch (RemoteException ex){
            System.out.println("Connection lost");
            synchronized (finishLock){
                finishLock.notify();
            }
        }
        return;
    }

    private String getAnswers(studentQuestion studentQuestion){
        int answer=-1;
        Scanner scan = new Scanner(System.in);
        List<String> answers= studentQuestion.getAnswers();
        printAnswers(answers);
        boolean correctValue=false;
        while (!correctValue) {
            try {
                answer = Integer.parseInt(scan.nextLine());
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
