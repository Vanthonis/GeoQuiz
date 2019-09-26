package edu.uwp.cs.csci323.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    // TODO: 9/25/2019 Challenge: Variables
    private static final String KEY_POS = "pos";
    private static final String KEY_PER = "per";
    private static final String KEY_BOL = "bol";
    private static final String KEY_CHEATER = "cheat";
    private static final String KEY_BOOLCHEAT = "boolcheat";
    private static final String KEY_CHEATED = "cheated";
    private boolean[] mBooleans = new boolean[mQuestionBank.length];
    
    // TODO: 9/25/2019  Chapter 5 Challenge:Variable to hold if the User Cheated on a Question
    private boolean[] mBooleanCheat = new boolean[mQuestionBank.length];

    private TextView mRemainCheats;
    private double mPlayerPercent = 0;
    private int mPlayerPosition = 0;
    public int mCheated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: 9/25/2019 Chapter 2 Challenge: Button to ImageButton(Variables)
        ImageButton mNextButton;
        ImageButton mPreviousButton;


        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(bundle) called");
        setContentView(R.layout.activity_main);

        // TODO: 9/25/2019 Chapter Challenges for Saving Variables on Screen Rotation
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mPlayerPercent = savedInstanceState.getDouble(KEY_PER, 0);
            mPlayerPosition = savedInstanceState.getInt(KEY_POS, 0);
            mBooleans = savedInstanceState.getBooleanArray(KEY_BOL);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER);
            mBooleanCheat = savedInstanceState.getBooleanArray(KEY_BOOLCHEAT);
            mCheated = savedInstanceState.getInt(KEY_CHEATED);
        }


        mQuestionTextView = findViewById((R.id.question_text_view));

        // TODO: 9/25/2019 Chapter 2 Challenge: Add a Listener to the TextView
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                percentQuestion();
                mBooleans[mCurrentIndex] = true;
                questionAnswered();
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                percentQuestion();
                mBooleans[mCurrentIndex] = true;
                questionAnswered();
            }
        });


        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = mBooleanCheat[mCurrentIndex];
                updateQuestion();

            }
        });
        updateQuestion();

        // TODO: 9/25/2019 Chapter 2 Challenge: Add a Previous Button
        mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex = mCurrentIndex - 1;
                } else {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                mIsCheater = mBooleanCheat[mCurrentIndex];
                updateQuestion();
            }
        });
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                mBooleanCheat[mCurrentIndex] = true;
                mCheated++;
                updateQuestion();
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
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
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
        savedInstanceState.putBooleanArray(KEY_BOOLCHEAT, mBooleanCheat);
        savedInstanceState.putInt(KEY_CHEATED, mCheated);
    }

    // TODO: 9/25/2019 Chapter 6 Challenge: Limitied Cheats(Method) 
    public void disableCheat() {
        if (mBooleanCheat[mCurrentIndex]) {
            if (mCheated > 2) {
                mCheatButton.setEnabled(false);
            }
        }
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
        disableCheat();
        mRemainCheats = findViewById(R.id.remaining_cheats);
        mRemainCheats.setText("You have " + (3 - mCheated) + " cheats remaining.");

        mQuestionTextView.setText(question);
        questionAnswered();
    }

    // TODO: 9/25/2019 Chapter 3 Challenge: Preventing Repeat Answers(Method)
    private void questionAnswered() {

        if (mBooleans[mCurrentIndex]) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }


    }

    // TODO: 9/25/2019 Chapter 3 Challenge: Graded Quiz(Method)
    private void percentQuestion() {
        double questAns;

        if (mPlayerPosition == mQuestionBank.length) {
            questAns = mPlayerPercent / mQuestionBank.length * 100;
            questAns = (int) questAns;
            Toast perToast = Toast.makeText(MainActivity.this, questAns + " Percent", Toast.LENGTH_SHORT);
            perToast.show();
        }


    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mPlayerPercent++;
                mPlayerPosition++;
            } else {
                messageResId = R.string.incorrect_toast;
                mPlayerPosition++;
            }
        }

        // TODO: 9/25/2019 Chapter 1 Challenge: Customizing Toast Gravity
        Toast myToast = Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        myToast.show();
    }
}
