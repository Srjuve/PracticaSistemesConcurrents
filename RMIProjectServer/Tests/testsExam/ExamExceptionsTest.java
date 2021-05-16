package testsExam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Exam;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExamExceptionsTest {
    Exam testedExam;
    @BeforeEach
    void initExam() throws IOException,exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
        this.testedExam=new Exam("./ExampleQuestions/questions.csv");
    }

    @Test
    void testIOException(){
        assertThrows(IOException.class,()->new Exam("notexistent"));
    }

    @Test
    void invalidQuestionAnswerFormat(){
        assertThrows(exceptions.invalidQuestionAnswerFormat.class,()->new Exam("./ExampleQuestions/incorrectquestions.csv"));
    }

    @Test
    void getNextQuestionNoQuestionsLeftException() throws exceptions.noCorrectAnswerAdded,exceptions.noQuestionsLeft{
        this.testedExam.answerActualQuestion("answer1");
        this.testedExam.answerActualQuestion("answer2");
        assertThrows(exceptions.noQuestionsLeft.class,()->this.testedExam.getNextQuestion());
    }

    @Test
    void sendAnswerNoQuestionsLeftException() throws exceptions.noCorrectAnswerAdded,exceptions.noQuestionsLeft{
        this.testedExam.answerActualQuestion("answer1");
        this.testedExam.answerActualQuestion("answer2");
        assertThrows(exceptions.noQuestionsLeft.class,()->this.testedExam.answerActualQuestion("answer1"));
    }

}