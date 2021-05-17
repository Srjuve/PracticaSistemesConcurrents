package testsQuestion;
import server.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class QuestionExceptionsTests {
    Question testedQuestion;
    @BeforeEach
    void init(){
        this.testedQuestion=new Question("Question");
    }

    @Test
    void setMultipleCorrectAnswersTest(){
        List<String> answersList = new ArrayList<>();
        answersList.add("Answer1");
        this.testedQuestion.setAnswers(answersList);
    }

    @Test
    void checkIfCorrectWithoutCorrectAnswers(){
        assertThrows(exceptions.noCorrectAnswerAdded.class,()->this.testedQuestion.isCorrectAnswer("Answer"));
    }
}
