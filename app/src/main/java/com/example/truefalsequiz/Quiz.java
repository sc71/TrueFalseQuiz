package com.example.truefalsequiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    private List<Question> questions;
    private int score, questionNum;

    public Quiz(List<Question> questions){
        questionNum = -1;
        this.questions = new ArrayList<>();
        int i = 0;
        for(Question q:questions){
            this.questions.add(q);
        }
        score = 0;
    }
    public Question getNextQuestion(){
        questionNum++;
        if(questionNum > questions.size() - 1){
            return null;
        }
        else{
            return questions.get(questionNum);
        }
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int add){
        score+=add;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

}
