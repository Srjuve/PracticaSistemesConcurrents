package server;

import common.Question;
import exceptions.invalidQuestionAnswerFormat;
import exceptions.noQuestionsLeft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;

public class Exam {
    private List<Question> questions;
    private double grade;
    private double pointsCorrectAnswer;

    public Exam(String csvFileName) throws IOException, exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
        createQuestions(csvFileName);
        grade=0;
    }

    private void createQuestions(String csvFileName) throws IOException, exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
        //Add the questions on the csv file provided by arguments
            BufferedReader fileReader = new BufferedReader(new FileReader(csvFileName));
            questions = new ArrayList<>();
            String line = "";
            int i=0;
            while ((line = fileReader.readLine()) != null) {
                String[] questionData = line.split(";");
                questions.add(getQuestion(questionData));
                i+=1;
            }
            pointsCorrectAnswer=10.0/questions.size();
    }

    private Question getQuestion(String[] questionData) throws exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException{
        //Get the question data(It is assumed that the question text its already correct)
        try{
            Question newQuestion = new Question(questionData[0]);
            List<String> answers = new ArrayList<>();
            for (int i = 1; i < questionData.length - 1; i += 1) {
                answers.add(questionData[i]);
            }
            newQuestion.setAnswers(answers);
            int correctAnswer=Integer.parseInt(questionData[questionData.length - 1])-1;
            if(correctAnswer>=questionData.length || correctAnswer<0)
                throw new exceptions.invalidQuestionAnswerFormat("Correct answer indicator does not point to the correct question");
            newQuestion.setCorrectAnswer(correctAnswer);
            return newQuestion;
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException ex){
            throw new exceptions.invalidQuestionAnswerFormat("Incorrect answer format");
        }
    }
    public Question getNextQuestion() throws noQuestionsLeft {
        //Returns the next question of the exam and, if there are no more questions returns the exception noQuestionsLeft
        if(questions!=null && questions.size()>0)
            return questions.get(0);
        throw new noQuestionsLeft("Questions haven't been added");
    }
    public void answerActualQuestion(String answer) throws exceptions.noCorrectAnswerAdded, noQuestionsLeft {
        //Takes the given answer, checks if there are more questions, if there are not, throws a noQuestionsLeft exception.
        //If there are adds the corresponding grade and removes the actual question.
        if(questions!=null && questions.size()>0){
            if (questions.get(0).isCorrectAnswer(answer)) {
                this.grade+=this.pointsCorrectAnswer;
            }
            questions.remove(0);
        }else {
            throw new noQuestionsLeft("Questions haven't been added");
        }
    }

    public double getGrades(){
        return this.grade;
    }
}
