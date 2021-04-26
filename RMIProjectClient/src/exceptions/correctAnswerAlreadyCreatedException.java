package exceptions;

public class correctAnswerAlreadyCreatedException extends Exception{
    public correctAnswerAlreadyCreatedException(String errorMessage){
        super(errorMessage);
    }
}
