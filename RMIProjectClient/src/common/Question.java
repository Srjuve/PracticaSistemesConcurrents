package common;

import exceptions.correctAnswerAlreadyCreatedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question implements Serializable {
    String question;
    List<String> answers;
    String correctAnswer;

    private Question(String question,List<String> answers,String correctAnswer){
        this.question=question;
        this.answers=answers;
        this.correctAnswer=correctAnswer;
    }

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

    public Question copyQuestion(){
        List<String> newAnswers = new ArrayList<>(this.answers);
        return new Question(this.question,newAnswers,correctAnswer);
    }

    private boolean checkAnswers(Question o){
        if(this.answers == null){
            return o.answers == null;
        }else{
            return this.answers.equals(o.answers);
        }
    }

    private boolean checkCorrectAnswer(Question o){
        if(this.correctAnswer == null){
            return o.correctAnswer == null;
        }else{
            return this.correctAnswer.equals(o.correctAnswer);
        }
    }
    @Override
    public boolean equals(Object o){
        if(o==this)
            return true;

        if(!(o instanceof Question))
            return false;

        return this.question.equals(((Question) o).question) && checkAnswers((Question)o) && checkCorrectAnswer((Question)o);
    }
}
