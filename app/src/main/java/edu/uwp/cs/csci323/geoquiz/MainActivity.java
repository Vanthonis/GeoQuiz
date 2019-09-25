package edu.uwp.cs.csci323.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_POS = "pos";
    private static final String KEY_PER = "per";
    private static final String KEY_BOL = "bol";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private double mPlayerPercent = 0;
    private int mPlayerPosition = 0;
    private boolean[] mBooleans = new boolean[6];

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mPlayerPercent = savedInstanceState.getDouble(KEY_PER, 0);
            mPlayerPosition = savedInstanceState.getInt(KEY_POS, 0);
            mBooleans = savedInstanceState.getBooleanArray(KEY_BOL);
        }

        mQuestionTextView = (TextView) findViewById((R.id.question_text_view));
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                percentQuestion();
                mBooleans[mCurrentIndex] = true;
                questionAnswered();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                percentQuestion();
                mBooleans[mCurrentIndex] = true;
                questionAnswered();
            }
        });


        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex = mCurrentIndex - 1;
                } else {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });
        mCheatButton = (Button)findViewById(R.id.)

        updateQuestion();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putDouble(KEY_PER, mPlayerPercent);
        savedInstanceState.putInt(KEY_POS, mPlayerPosition);
        savedInstanceState.putBooleanArray(KEY_BOL, mBooleans);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        questionAnswered();
    }

    private void questionAnswered() {

        if (mBooleans[mCurrentIndex] == true) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }


    }

    private void percentQuestion() {
        double questAns = 0;

        if (mPlayerPosition == mQuestionBank.length) {
            questAns = mPlayerPercent / mQuestionBank.length * 100;
            questAns = (int) questAns;
            Toast perToast = Toast.makeText(MainActivity.this, String.valueOf(questAns) + " Percent", Toast.LENGTH_SHORT);
            perToast.show();
        }


    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mPlayerPercent++;
            mPlayerPosition++;
        } else {
            messageResId = R.string.incorrect_toast;
            mPlayerPosition++;
        }
        Toast myToast = Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        myToast.show();
    }
}
