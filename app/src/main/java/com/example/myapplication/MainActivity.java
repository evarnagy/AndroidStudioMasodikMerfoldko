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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY= 1;
    EditText userNameET;
    EditText passwordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



         userNameET = findViewById(R.id.ediTextUserName);
         passwordET = findViewById(R.id.ediTextPasswordName);

         preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
         mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView myText = findViewById(R.id.ediTextUserName);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_inrow);
        myText.startAnimation(fadeIn);

    }

    public void login(View view) {

        String userNameStr = userNameET.getText().toString();
        String passwordStr = passwordET.getText().toString();

       // Log.i(LOG_TAG, "Bejelentkezett: " + userNameStr + "jelszó:" + passwordStr);

        mAuth.signInWithEmailAndPassword(userNameStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
              if(task.isSuccessful())
              {
                  Log.d(LOG_TAG, "User login sucessful ");
                  startQuestionList();
              }else
              {
                  Log.d(LOG_TAG, "User login fail ");
                  Toast.makeText(MainActivity.this,"User login fail" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
              }
            }
        });

    }
    private void startQuestionList()
    {
        Intent intent = new Intent(this, QuestionListActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", 1);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"OnCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"OnDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"OnPause");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",userNameET.getText().toString());
        editor.putString("password",passwordET.getText().toString());
        editor.apply();

        Log.i(LOG_TAG,"OnResume");
    }

    public void loginAsGuest(View view)
    {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Log.d(LOG_TAG, "Anonim user  login sucessful ");
                    startQuestionList();
                }else
                {
                    Log.d(LOG_TAG, "Anonym user login fail ");
                    Toast.makeText(MainActivity.this,"User login fail" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginWithGoogle(View view) {
        // TODO
    }
}