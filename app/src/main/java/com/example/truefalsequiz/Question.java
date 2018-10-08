package com.example.truefalsequiz;

public class Question {
    private String question, explanation;
    private boolean answer;

    public Question(String question, boolean answer, String explanation){
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
    }

    public Boolean checkAnswer(boolean answer){
        if(answer == this.answer){
            return true;
        }
        else{
            return false;
        }
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswer(){
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }
}
