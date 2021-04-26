package exceptions;

public class noCorrectAnswerAdded extends Exception{
    public noCorrectAnswerAdded(String errorMessage){
        super(errorMessage);
    }
}
