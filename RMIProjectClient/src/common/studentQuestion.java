package common;

import java.io.Serializable;
import java.util.List;

public class studentQuestion implements Serializable {
    //This class is used to send only the necessary data to the Students.
    String question;
    List<String> answers;

    public studentQuestion(String question, List<String> answers){
        this.question=question;
        this.answers=answers;
    }

    public String getQuestion(){
        return this.question;
    }
    public List<String> getAnswers(){
        return this.answers;
    }

}