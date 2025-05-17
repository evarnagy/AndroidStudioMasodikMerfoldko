package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 1;
    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordagainEditText;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.i(LOG_TAG,"OnCreate");
       int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

       if(secret_key != 1)
       {
           finish();
       }
        userNameEditText = findViewById(R.id.usernameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passWordEditText);
        passwordagainEditText = findViewById(R.id.passWordAgainEditText);
        SharedPreferences preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName =  preferences.getString("userName","");
        String password = preferences.getString("password","");
        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        passwordagainEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();


    }

    public void register(View view)
    {
        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordagainEditText.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {

            Toast.makeText(this, "Minden mezőt ki kell tölteni!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordConfirm)) {

            Toast.makeText(this, "A jelszó és a megerősítés nem egyezik!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(LOG_TAG, "Regisztrált: " + userName + "email:" + email);

        //startQuestionList();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d(LOG_TAG, "User created succesfully");
                    startQuestionList();
                }else
                {
                    Log.d(LOG_TAG, "User wasnt created succesfully");
                    Toast.makeText(RegisterActivity.this,"USer wasnt created" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
    private void startQuestionList(/* user data */)
    {
        Intent intent = new Intent(this, QuestionListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}