package com.example.truefalsequiz;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static android.view.View.VISIBLE;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonTrue, buttonFalse, buttonNext;
    private TextView textViewScore, textViewQuestionNum, textViewQuestion, textViewExplanation;
    private TextView textViewQuestionGone;
    private Quiz quiz;
    private Question question;
    private int questionNum;
    private boolean show;

    public static final String SCORE_CONST = "score_constant";
    public static final int INTENT_CODE = 1;
    public static final String QUESTION = "QUESTION";
    public static final String SCORE = "SCORE";
    public static final String QUESTION_NUM = "QUESTION-NUM";
    public static final String QUIZ = "QUIZ";

    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet = new ConstraintSet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        wireWidgets();
        setListeners();
        initializeQuiz();
        String text = getString(R.string.score) + "0";
        textViewScore.setText(text);
    }

    private void initializeQuiz() {
        InputStream inputStream = getResources().openRawResource(R.raw.questions);
        String jsonString = readTextFile(inputStream);
        Gson gson = new Gson();
        Question[] questions = gson.fromJson(jsonString, Question[].class);
        List<Question> questionList = Arrays.asList(questions);
        quiz = new Quiz(questionList);
        question = quiz.getNextQuestion();
        textViewQuestion.setText(question.getQuestion());
        questionNum = 1;
    }

    private void wireWidgets() {
        buttonTrue = findViewById(R.id.button_quiz_true);
        buttonFalse = findViewById(R.id.button_quiz_false);
        buttonNext = findViewById(R.id.button_quiz_next);
        textViewQuestion = findViewById(R.id.textview_quiz_question_gone);
        textViewQuestionGone = findViewById(R.id.textview_quiz_question_gone);
        textViewQuestionNum = findViewById(R.id.textview_quiz_question_num);
        textViewScore = findViewById(R.id.textview_quiz_score);
        textViewExplanation = findViewById(R.id.textview_quiz_explanation);
        constraintLayout = findViewById(R.id.constraint_layout_quiz_main);
    }

    private void setListeners() {
        buttonTrue.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonFalse.setOnClickListener(this);
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_quiz_true:
                buttonBasicFunction(true, buttonTrue, buttonFalse);
                break;
            case R.id.button_quiz_false:
                buttonBasicFunction(false, buttonFalse, buttonTrue);
                break;
            case R.id.button_quiz_next:
                buttonNext.setText("");
                hideComponents();
                buttonFalse.setEnabled(true);
                buttonTrue.setEnabled(true);
                textViewExplanation.setText("");
                question = quiz.getNextQuestion();
                if (question != null) {
                    textViewQuestion.setText(question.getQuestion());
                    textViewQuestionGone.setText(question.getQuestion());
                    questionNum++;
                    String text = getString(R.string.question) + questionNum;
                    textViewQuestionNum.setText(text);
                    buttonTrue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    buttonFalse.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Intent intent = new Intent(QuizActivity.this, FinalScoreActivity.class);
                    intent.putExtra(SCORE_CONST, quiz.getScore());
                    startActivityForResult(intent, INTENT_CODE);
                }
                break;
        }

    }

    private void buttonBasicFunction(boolean b, Button button, Button otherButton) {
        buttonNext.setText(R.string.next);
        showComponents();
        textViewExplanation.setText(question.getExplanation());
        buttonFalse.setEnabled(false);
        buttonTrue.setEnabled(false);
        if (question.checkAnswer(b)) {
            button.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            otherButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
            quiz.addToScore(100);
            String scoreText = getString(R.string.score) + quiz.getScore();
            textViewScore.setText(scoreText);
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.colorRed));
            otherButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(QUESTION, question);
        outState.putInt(QUESTION_NUM, questionNum);
        outState.putSerializable(QUIZ, quiz);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        question = (Question) savedInstanceState.getSerializable(QUESTION);
        questionNum = savedInstanceState.getInt(QUESTION_NUM);
        quiz = (Quiz) savedInstanceState.getSerializable(QUIZ);
        String text = getString(R.string.question) + questionNum;
        textViewQuestionNum.setText(text);
        textViewQuestion.setText(question.getQuestion());
        if(buttonNext.getVisibility() == VISIBLE){
            textViewExplanation.setText(question.getExplanation());
        }
        String txt = "Score: " + quiz.getScore();
        textViewScore.setText(txt);
    }


    private void showComponents(){

        constraintSet.clone(this, R.layout.activity_quiz_next_gone);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000);

        TransitionManager.beginDelayedTransition(constraintLayout, transition);
        constraintSet.applyTo(constraintLayout);
    }

    private void hideComponents(){

        constraintSet.clone(this, R.layout.activity_quiz);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000);


        TransitionManager.beginDelayedTransition(constraintLayout, transition);
        constraintSet.applyTo(constraintLayout);
    }

}
