package testsQuestion;

import common.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionFuncTest {
    Question testedQuestion;
    @BeforeEach
    void init(){
        this.testedQuestion=new Question("Question");
    }

    @Test
    void setAnswersTest(){
        List<String> answers = new ArrayList<>();
        answers.add("Answer1");
        answers.add("Answer2");
        this.testedQuestion.setAnswers(answers);
        assertEquals(answers,this.testedQuestion.getAnswers());
    }

    @Test
    void testCorrectAnswer() throws exceptions.correctAnswerAlreadyCreatedException,exceptions.noCorrectAnswerAdded{
        List<String> answers = new ArrayList<>();
        answers.add("Answer1");
        answers.add("Answer2");
        this.testedQuestion.setAnswers(answers);
        this.testedQuestion.setCorrectAnswer(0);
        assertTrue(this.testedQuestion.isCorrectAnswer("Answer1"));
        assertFalse(this.testedQuestion.isCorrectAnswer("Answer2"));
    }

    @Test
    void getQuestionTest(){
        assertEquals("Question",this.testedQuestion.getQuestion());
    }

    @Test
    void copyQustionTest(){
        assertEquals(this.testedQuestion,this.testedQuestion.copyQuestion());
    }

}
