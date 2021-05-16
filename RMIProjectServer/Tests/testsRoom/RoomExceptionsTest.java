package testsRoom;

import common.Room;
import common.Student;
import dummys.SimpleStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.RoomImplementation;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RoomExceptionsTest {
    RoomImplementation testedRoom;

    @BeforeEach
    void initRoom() throws RemoteException {
        this.testedRoom=new RoomImplementation();
    }

    @Test
    void registerStudentRoomNotStarted() throws RemoteException{
        Student newStudent = new SimpleStudent(12);
        assertThrows(exceptions.notAcceptingStudentsException.class,()->this.testedRoom.joinExam(12,newStudent));
    }

    @Test
    void setNotExistentCSV() throws RemoteException{
        assertThrows(IOException.class,()->this.testedRoom.setCSVFile("NotExistent"));
    }

    @Test
    void setIncorrectCSV() throws RemoteException{
        assertThrows(exceptions.invalidQuestionAnswerFormat.class,()->this.testedRoom.setCSVFile("./ExampleQuestions/incorrectquestions.csv"));
    }

    @Test
    void registerStudentAlreadyInitiatedExam() throws RemoteException, IOException, exceptions.invalidQuestionAnswerFormat,exceptions.noQuestionsLeft,exceptions.alreadyStartedException{
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        this.testedRoom.startExam();
        Student newStudent = new SimpleStudent(12);
        assertThrows(exceptions.notAcceptingStudentsException.class,()->this.testedRoom.joinExam(12,newStudent));
    }

    @Test
    void startAlreadyStartedExam() throws RemoteException,IOException,exceptions.invalidQuestionAnswerFormat,exceptions.noQuestionsLeft,exceptions.alreadyStartedException{
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        this.testedRoom.startExam();
        assertThrows(exceptions.alreadyStartedException.class,()->this.testedRoom.startExam());
    }

}