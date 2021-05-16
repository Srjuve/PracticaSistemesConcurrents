package testsRoom;

import common.Student;
import dummys.SimpleStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.RoomImplementation;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RoomFuncTest {
    RoomImplementation testedRoom;
    @BeforeEach
    void initRoom() throws RemoteException {
        this.testedRoom=new RoomImplementation();
    }

    @Test
    void testOpenExam() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        assertTrue(this.testedRoom.getAcceptingStudents());
        this.testedRoom.startExam();
        assertFalse(this.testedRoom.getAcceptingStudents());
    }

    @Test
    void testStartExam() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        SimpleStudent testedStudent = new SimpleStudent(12);
        this.testedRoom.joinExam(12,testedStudent);
        this.testedRoom.startExam();
        assertTrue(testedRoom.getStarted());
        assertEquals(testedStudent.recievedQuestion.getQuestion(),"Question1?");
    }

    @Test
    void testJoinedStudents() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat,exceptions.notAcceptingStudentsException,
    exceptions.studentAlreadyJoinedException,exceptions.examErrorException {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        assertEquals(0,this.testedRoom.getJoinedStudentsNumbers());
        SimpleStudent testedStudent = new SimpleStudent(12);
        this.testedRoom.joinExam(12,testedStudent);
        assertEquals(1,this.testedRoom.getJoinedStudentsNumbers());
    }
    @Test
    void testSendAnswer() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        SimpleStudent testedStudent = new SimpleStudent(12);
        this.testedRoom.joinExam(12,testedStudent);
        this.testedRoom.startExam();
        assertEquals(testedStudent.recievedQuestion.getQuestion(),"Question1?");
        this.testedRoom.sendAnswer(12,"exemple");
        assertEquals(testedStudent.recievedQuestion.getQuestion(),"Question2?");
    }

    @Test
    void testFinishByAnswer() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        SimpleStudent testedStudent = new SimpleStudent(12);
        this.testedRoom.joinExam(12,testedStudent);
        this.testedRoom.startExam();
        this.testedRoom.sendAnswer(12,"Answer1");
        this.testedRoom.sendAnswer(12,"exemple");
        assertEquals(5,testedStudent.grade);
    }
    @Test
    void testFinishExam() throws RemoteException, IOException,exceptions.noQuestionsLeft,exceptions.alreadyStartedException,exceptions.invalidQuestionAnswerFormat,exceptions.notAcceptingStudentsException,
            exceptions.studentAlreadyJoinedException,exceptions.examErrorException {
        this.testedRoom.setCSVFile("./ExampleQuestions/questions.csv");
        this.testedRoom.startAcceptingStudents();
        SimpleStudent testedStudent = new SimpleStudent(12);
        this.testedRoom.joinExam(12,testedStudent);
        this.testedRoom.startExam();
        this.testedRoom.finishExam();
        assertEquals(0,testedStudent.grade);
    }

}