package common;

import java.rmi.Remote;

public interface Student extends Remote {
    void sendQuestion(Question givenQuestion);
    void finishExam(float grade);

}
