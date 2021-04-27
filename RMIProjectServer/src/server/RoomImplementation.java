package server;

import common.Question;
import common.Room;
import common.Student;
import exceptions.noQuestionsLeft;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RoomImplementation extends UnicastRemoteObject implements Room {
    private HashMap<Integer, Exam> actualExams;
    private HashMap<Integer, Student> actualStudents;
    private HashMap<Integer, Double> studentsGrades;
    private boolean acceptingStudents;

    public RoomImplementation() throws RemoteException{
        actualExams = new HashMap<>();
        actualStudents = new HashMap<>();
        studentsGrades = new HashMap<>();
        acceptingStudents=false;
    }

    public void startAcceptingStudents(){
        acceptingStudents=true;
    }

    public void startExam() throws noQuestionsLeft,RemoteException{
        synchronized (this) {
            acceptingStudents = false;
            Set<Integer> allStudentsID = actualStudents.keySet();
            Iterator<Integer> it = allStudentsID.iterator();
            while (it.hasNext()) {
                Integer actualID = it.next();
                Question newQuestion = actualExams.get(actualID).getNextQuestion();
                System.out.println(newQuestion.getQuestion());
                actualStudents.get(actualID).sendQuestion(newQuestion);
            }
        }
    }

    public int getJoinedStudentsNumbers(){
        return actualStudents.size();
    }

    public void joinExam(Integer universityId, Student joinedStudent) throws RemoteException,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException{
        synchronized(this) {
            if (acceptingStudents) {
                if(!actualStudents.containsKey(universityId)){
                    try {
                        actualExams.put(universityId, new Exam("/home/ajs/Escriptori/UDL/COMPUDIST/Practica1/questions.csv"));
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
        synchronized(this) {
            try {
                if(actualStudents.containsKey(universityID)) {
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
