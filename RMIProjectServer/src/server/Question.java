package server;

import java.util.ArrayList;
import java.util.List;

public class Question{
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
        this.answers=new ArrayList<>();
    }

    public void setAnswers(List<String> answers){
        this.answers = answers;
    }

    public void setCorrectAnswer(int number) throws exceptions.correctAnswerAlreadyCreatedException{
        //We set the correct answer of the question. It is assumed that the number will point to a valid answer.
        if(correctAnswer==null) {
            correctAnswer = answers.get(number);
        }else{
            throw new exceptions.correctAnswerAlreadyCreatedException("Correct answer already picked");
        }
    }

    public String getQuestion(){
        return this.question;
    }

    public List<String> getAnswers(){
        return this.answers;
    }

    public boolean isCorrectAnswer(String answer) throws exceptions.noCorrectAnswerAdded{
        //Check if the given string is equal to the correct answer. If no correct answer has been set we return the noCorrectAnswerAdded Exception
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
