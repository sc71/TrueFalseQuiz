package com.example.truefalsequiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.truefalsequiz.QuizActivity.SCORE_CONST;

public class FinalScoreActivity extends AppCompatActivity {

    public static final String RESTART_CODE = "restart";
    private Button buttonAgain;
    private TextView textViewScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);
        buttonAgain = findViewById(R.id.button_finalscore_playagain);
        textViewScore = findViewById(R.id.textview_finalscore_scorenum);
        textViewScore.setText("" + getIntent().getIntExtra(SCORE_CONST, 0));
        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(FinalScoreActivity.this, QuizActivity.class);
                                    //returnIntent.putExtra(RESTART_CODE, 9);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                    startActivity(returnIntent);
            }
        });
    }
}
