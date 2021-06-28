package com.spawner.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.spawner.triviaapp.data.AnswerListAsyncResponse;
import com.spawner.triviaapp.data.QuestionBank;
import com.spawner.triviaapp.model.Question;
import com.spawner.triviaapp.model.Score;
import com.spawner.triviaapp.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextview;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionlist;
    private TextView scoreTextView;
    private TextView highestScore;

    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();

        prefs = new Prefs(MainActivity.this);

        highestScore = findViewById(R.id.highest_score);
        scoreTextView = findViewById(R.id.score_text);
        questionTextview = findViewById(R.id.queston_textview);
        questionCounterTextView = findViewById(R.id.counter_text);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);

        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        //get previous state
        currentQuestionIndex = prefs.getState();
        //Log.d("savestate", "onCreate: "+prefs.getState());
        //Shows current Score
        scoreTextView.setText(String.valueOf(MessageFormat.format("Current Score: {0}", score.getScore())));
        //Shows Highest score stored in SharedPrefs
        highestScore.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(prefs.getHighScore())));


        questionlist = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                Log.d("vedant", "processFinished: "+questionArrayList);

            }
        });
        //Log.d("vedant", "onCreate: "+questionList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex -1) % questionlist.size();
                    updateQuestion();
                } else {
                    Toast.makeText(MainActivity.this,"There is no 0th Question!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.next_button:
                goNext();
                break;
        }

    }
    private void updateQuestion() {
        String question = questionlist.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionlist.size()));
    }

    private void checkAnswer(boolean userCorrect) {
        boolean answerIsTrue = questionlist.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if(userCorrect == answerIsTrue) {
            fadeView();
            addPoint();
            toastMessageId = R.string.correct_answer;
        } else {
            shakeAnimation();
            deductPoint();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastMessageId,Toast.LENGTH_SHORT).show();
    }

    private void addPoint() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreTextView.setText(String.valueOf(MessageFormat.format("Current Score: {0}", score.getScore())));
    }//Add 100 points if the answer is correct.

    private void deductPoint() {
        if (scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            scoreTextView.setText(String.valueOf(MessageFormat.format("Current Score: {0}", score.getScore())));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            scoreTextView.setText(String.valueOf(MessageFormat.format("Current Score: {0}", score.getScore())));
        }
    }//Subtract 100 points if the answer if wrong.

    private void fadeView () {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }//fades the cardView into Green color if the answer is TRUE

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView =findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }//shakes the cardView into Red color if the answer is FALSE

    public void goNext() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionlist.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }//Saves the highest score on SharedPreferences
}