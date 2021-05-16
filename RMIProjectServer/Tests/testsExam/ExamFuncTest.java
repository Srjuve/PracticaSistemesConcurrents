package testsExam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Exam;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExamFuncTest {
    Exam testedExam;
    @BeforeEach
    void initExam() throws IOException,exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
        this.testedExam=new Exam("./ExampleQuestions/questions.csv");
    }

    @Test
    void testGetQuestion() throws exceptions.noQuestionsLeft,exceptions.noCorrectAnswerAdded{
        assertEquals("Question1?",this.testedExam.getNextQuestion().getQuestion());
        this.testedExam.answerActualQuestion("potatoe");
        assertEquals("Question2?",this.testedExam.getNextQuestion().getQuestion());
    }

    @Test
    void testAnswerQuestionCorrectAnswers() throws exceptions.noQuestionsLeft,exceptions.noCorrectAnswerAdded
    {
        this.testedExam.answerActualQuestion("Answer1");
        assertEquals(5,this.testedExam.getGrades());
        this.testedExam.answerActualQuestion("Answer2");
        assertEquals(10,this.testedExam.getGrades());
    }

    @Test
    void testAnswerQuestionIncorrectAnswers() throws exceptions.noQuestionsLeft,exceptions.noCorrectAnswerAdded
    {
        this.testedExam.answerActualQuestion("Incorrect");
        assertEquals(0,this.testedExam.getGrades());
        this.testedExam.answerActualQuestion("Incorrect");
        assertEquals(0,this.testedExam.getGrades());
    }

    @Test
    void copyExamTest(){
        assertEquals(this.testedExam,this.testedExam.copyExam());
    }
}