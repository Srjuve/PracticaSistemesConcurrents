package common;

import exceptions.correctAnswerAlreadyCreatedException;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    String question;
    List<String> answers;
    String correctAnswer;

    public Question(String question){
        this.question = question;
    }

    public void setAnswers(List<String> answers){
        this.answers = answers;
    }

    public void setCorrectAnswer(int number) throws correctAnswerAlreadyCreatedException{
        if(correctAnswer==null) {
            correctAnswer = answers.get(number);
        }else{
            throw new correctAnswerAlreadyCreatedException("Correct answer already picked");
        }
    }

    public String getQuestion(){
        return this.question;
    }
    public List<String> getAnswers(){
        return this.answers;
    }
    public boolean isCorrectAnswer(String answer) throws exceptions.noCorrectAnswerAdded{
        if(correctAnswer!=null && answer!=null)
            return answer.equals(this.correctAnswer);
        throw new exceptions.noCorrectAnswerAdded("No correct Answer Added");
    }
}
