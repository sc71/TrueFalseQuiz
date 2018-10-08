package com.example.truefalsequiz;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        InputStream inputStream = getResources().openRawResource(R.raw.questions);
        String jsonString = readTextFile(inputStream);
        Gson gson = new Gson();
        Question[] questions =  gson.fromJson(jsonString, Question[].class);
        List<Question> questionList = Arrays.asList(questions);
//        String questionscheck = "";
//        String answerscheck = "";
//        String explanationscheck = "";
//        for(Question q: questionList){
//            questionscheck += " " + q.getQuestion();
//            answerscheck += " " + q.getAnswer();
//            explanationscheck += " " + q.getExplanation();
//        }
//        Log.d("LOOK HERE", "onCreate: " + questionscheck + answerscheck + explanationscheck);

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
}
