package server;

import common.Room;
import common.Student;
import common.studentQuestion;
import exceptions.noQuestionsLeft;
import exceptions.alreadyStartedException;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RoomImplementation extends UnicastRemoteObject implements Room {
    private HashMap<Integer, Exam> actualExams;
    private HashMap<Integer, Student> actualStudents;
    private HashMap<Integer, Double> studentsGrades;
    private Exam examTemplate;
    private String csvRoute;
    private boolean acceptingStudents;
    private boolean started;

    public RoomImplementation() throws RemoteException{
        actualExams = new HashMap<>();
        actualStudents = new HashMap<>();
        studentsGrades = new HashMap<>();
        acceptingStudents=false;
        started=false;
    }
    public boolean getAcceptingStudents(){
        return this.acceptingStudents;
    }

    public boolean getStarted(){
        return this.started;
    }
    public void setCSVFile(String csvRoute) throws exceptions.invalidQuestionAnswerFormat, IOException {
        this.csvRoute=csvRoute;
        try {
            this.examTemplate = new Exam(this.csvRoute);
        }catch (exceptions.correctAnswerAlreadyCreatedException ex){
            throw new exceptions.invalidQuestionAnswerFormat(ex.getMessage());
        }
    }

    public void startAcceptingStudents(){
        //Start accepting the registration of Students
        acceptingStudents=true;
    }

    public void startExam() throws noQuestionsLeft,RemoteException,alreadyStartedException{
        //Stop accepting students, and send the first question to every registered Student
        synchronized (this) {
            if(!this.started) {
                this.started=true;
                acceptingStudents = false;
                Set<Integer> allStudentsID = actualStudents.keySet();
                Iterator<Integer> it = allStudentsID.iterator();
                while (it.hasNext()) {
                    Integer actualID = it.next();
                    Question newQuestion = actualExams.get(actualID).getNextQuestion();
                    actualStudents.get(actualID).sendQuestion(new studentQuestion(newQuestion.getQuestion(),newQuestion.getAnswers()));
                }
            }else{
                throw new alreadyStartedException("The exam has already started");
            }
        }
    }

    public int getJoinedStudentsNumbers(){
        return actualStudents.size();
    }

    public void joinExam(Integer universityId, Student joinedStudent) throws RemoteException,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException{
        //Remote Function: The student with ID universityId joins the exam.Send exceptions if anything goes wrong.
        synchronized(this) {
            if (acceptingStudents) {
                if(!actualStudents.containsKey(universityId)){
                        actualExams.put(universityId,this.examTemplate.copyExam());
                        actualStudents.put(universityId,joinedStudent);
                        System.out.println("Student with ID "+universityId.toString()+" joined.");
                        System.out.println("Number of students joined: "+String.valueOf(getJoinedStudentsNumbers()));
                }else{
                    throw new exceptions.studentAlreadyJoinedException("This student already joined the room");
                }
            }else {
                throw new exceptions.notAcceptingStudentsException("The room is not accepting new Students");
            }
        }
    }

    public void finishExam() throws RemoteException{
        //Finish the exam for every registered Student that hasn't already finished.
        Set<Integer> allStudentsID = actualStudents.keySet();
        Iterator<Integer> it = allStudentsID.iterator();
        synchronized(this) {
            while (it.hasNext()) {

                    Integer actualID = it.next();
                    if (!studentsGrades.containsKey(actualID)) {
                        double studentGrade = actualExams.get(actualID).getGrades();
                        try {
                            actualStudents.get(actualID).finishExam(studentGrade);
                        }catch (ConnectException ex){}
                        studentsGrades.put(actualID, studentGrade);
                    }
            }
        }
    }

    public HashMap<Integer, Double> returnGrades(){
        return studentsGrades;
    }


    public void sendAnswer(Integer universityID, String answer) throws RemoteException{
        //Remote Function: The student sends the answer and, if there are questions left send them and, if not, finish the Student exam
        synchronized(this) {
            try {
                if(actualStudents.containsKey(universityID) && !studentsGrades.containsKey(universityID)) {
                    actualExams.get(universityID).answerActualQuestion(answer);
                    Question newQuestion = actualExams.get(universityID).getNextQuestion();
                    actualStudents.get(universityID).sendQuestion(new studentQuestion(newQuestion.getQuestion(),newQuestion.getAnswers()));
                }
            } catch (exceptions.noQuestionsLeft ex) {
                if (!studentsGrades.containsKey(universityID)) {
                    double studentGrade = actualExams.get(universityID).getGrades();
                    actualStudents.get(universityID).finishExam(studentGrade);
                    studentsGrades.put(universityID,studentGrade);
                }
            } catch (exceptions.noCorrectAnswerAdded ex) {
                System.exit(-1);
            }
        }
    }
}
