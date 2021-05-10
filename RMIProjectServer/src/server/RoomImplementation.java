package server;

import common.Question;
import common.Room;
import common.Student;
import exceptions.invalidQuestionAnswerFormat;
import exceptions.noQuestionsLeft;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RoomImplementation extends UnicastRemoteObject implements Room {
    private HashMap<Integer, Exam> actualExams;
    private HashMap<Integer, Student> actualStudents;
    private HashMap<Integer, Double> studentsGrades;
    private String csvRoute;
    private boolean acceptingStudents;

    public RoomImplementation() throws RemoteException{
        actualExams = new HashMap<>();
        actualStudents = new HashMap<>();
        studentsGrades = new HashMap<>();
        acceptingStudents=false;
    }
    public void setCSVFile(String csvRoute) throws exceptions.invalidQuestionAnswerFormat, IOException {
        this.csvRoute=csvRoute;
        checkValidQuestionsFile();
    }

    private void checkValidQuestionsFile() throws exceptions.invalidQuestionAnswerFormat, IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(this.csvRoute));
        String line = "";
        int i=0;
        while ((line = fileReader.readLine()) != null) {
            String[] questionData = line.split(";");
            checkQuestion(questionData);
            i+=1;
        }
        if(i==0){
            throw new exceptions.invalidQuestionAnswerFormat("No questions added");
        }
    }

    private void checkQuestion(String[] questionData) throws exceptions.invalidQuestionAnswerFormat{
        //Check if the question has a valid format
        if(questionData.length>=3){
            try{
                int number=Integer.parseInt(questionData[questionData.length - 1]);
                if(number<=0 || number>=questionData.length-1){
                    throw new invalidQuestionAnswerFormat("Correct answer indicator does not point to the correct question");
                }
            }catch(NumberFormatException ex){
                throw new invalidQuestionAnswerFormat("Correct answer indicator is not an Integer");
            }
        }else{
            throw new invalidQuestionAnswerFormat("Minimum question/answer fields required");
        }
    }

    public void startAcceptingStudents(){
        //Start accepting the registration of Students
        acceptingStudents=true;
    }

    public void startExam() throws noQuestionsLeft,RemoteException{
        //Stop accepting students, and send the first question to every registered Student
        synchronized (this) {
            acceptingStudents = false;
            Set<Integer> allStudentsID = actualStudents.keySet();
            Iterator<Integer> it = allStudentsID.iterator();
            while (it.hasNext()) {
                Integer actualID = it.next();
                Question newQuestion = actualExams.get(actualID).getNextQuestion();
                actualStudents.get(actualID).sendQuestion(newQuestion);
            }
        }
    }

    public int getJoinedStudentsNumbers(){
        return actualStudents.size();
    }

    public void joinExam(Integer universityId, Student joinedStudent) throws RemoteException,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException{
        //Remote Function: The student with ID universityId joins the exam.Send exceptions if anything goes wrong.
        synchronized(this) {
            if (acceptingStudents) {
                if(!actualStudents.containsKey(universityId)){
                    try {
                        actualExams.put(universityId, new Exam(this.csvRoute));
                        actualStudents.put(universityId,joinedStudent);
                        System.out.println("Student with ID "+universityId.toString()+" joined.");
                        System.out.println("Number of students joined: "+String.valueOf(getJoinedStudentsNumbers()));
                    }catch (Exception ex){
                        throw new exceptions.examErrorException(ex.getMessage());
                    }
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
                    actualStudents.get(actualID).finishExam(studentGrade);
                    studentsGrades.put(actualID,studentGrade);
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
                    actualStudents.get(universityID).sendQuestion(newQuestion);
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
