package server;

import common.Question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;

public class Exam {
    List<Question> questions;
    float grade;
    float pointsCorrectAnswer;

    public Exam(String csvFileName) throws IOException, exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
        createQuestions(csvFileName);
        grade=0;
    }

    private void createQuestions(String csvFileName) throws IOException, exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException {
            BufferedReader fileReader = new BufferedReader(new FileReader(csvFileName));
            questions = new ArrayList<>();
            String line = "";
            int i=0;
            while ((line = fileReader.readLine()) != null) {
                String[] questionData = line.split(";");
                questions.add(getQuestion(questionData));
                i+=1;
            }
            pointsCorrectAnswer=(1*questions.size())/10;
    }

    private Question getQuestion(String[] questionData) throws exceptions.invalidQuestionAnswerFormat,exceptions.correctAnswerAlreadyCreatedException{
        try{
            Question newQuestion = new Question(questionData[0]);
            List<String> answers = new ArrayList<>();
            for (int i = 1; i < questionData.length - 1; i += 1) {
                answers.add(questionData[i]);
            }
            newQuestion.setAnswers(answers);
            int correctAnswer=Integer.parseInt(questionData[questionData.length - 1])-1;
            if(correctAnswer>=questionData.length || correctAnswer<0)
                throw new exceptions.invalidQuestionAnswerFormat("The correct answer indicator cannot be negative");
            newQuestion.setCorrectAnswer(correctAnswer);
            return newQuestion;
        }catch (Exception ex){
            throw new exceptions.invalidQuestionAnswerFormat("Incorrect answer format");
        }
    }
    public Question getNextQuestion() throws exceptions.noQuestionsAdded{
        if(questions!=null && questions.size()>0)
            return questions.get(0);
        throw new exceptions.noQuestionsAdded("Questions haven't been added");
    }
    public void answerActualQuestion(String answer) throws exceptions.noCorrectAnswerAdded,exceptions.noQuestionsAdded{
        if(questions!=null && questions.size()>0){
            if (questions.get(0).isCorrectAnswer(answer)) {
                this.grade+=this.grade+this.pointsCorrectAnswer;
            }
            questions.remove(0);
        }
        throw new exceptions.noQuestionsAdded("Questions haven't been added");
    }

    public float getGrades(){
        return this.grade;
    }
}
