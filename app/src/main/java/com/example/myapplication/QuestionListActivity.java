package com.example.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;


public class QuestionListActivity extends AppCompatActivity {

    private EditText answer1EditText;
    private AlarmManager mManager;
    private RadioGroup answer2RadioGroup;
    private EditText answer3EditText;
    private ImageView imageView;
    private TextView locationTextView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private  NotificationHandler mNotificationHandler;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        imageView = findViewById(R.id.imageView);
        locationTextView = findViewById(R.id.locationTextView);

        answer1EditText = findViewById(R.id.answer1);
        answer2RadioGroup = findViewById(R.id.answer2);
        answer3EditText = findViewById(R.id.answer3);

        Button submitButton = findViewById(R.id.submit_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button CRUDButton = findViewById(R.id.openCrudButton);

        db = FirebaseFirestore.getInstance();

        cancelButton.setOnClickListener(view -> finish());

        submitButton.setOnClickListener(view -> submitSurvey());
        CRUDButton.setOnClickListener(view -> {
            Intent intent = new Intent(QuestionListActivity.this, SurveyCrudActivity.class);
            startActivity(intent);
        });


        Button cameraButton = findViewById(R.id.take_photo_button);
        cameraButton.setOnClickListener(view -> dispatchTakePictureIntent());


        requestLocationPermission();
        mNotificationHandler = new NotificationHandler(this);
        mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        setAlarmManager();




    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Object imageBitmap = extras.get("data");
                imageView.setImageBitmap((android.graphics.Bitmap) imageBitmap);
            }
        }
    }

    private void submitSurvey() {
        String favoriteColor = answer1EditText.getText().toString();
        int selectedCarTypeId = answer2RadioGroup.getCheckedRadioButtonId();
        RadioButton selectedCarTypeButton = findViewById(selectedCarTypeId);
        String carType = selectedCarTypeButton != null ? selectedCarTypeButton.getText().toString() : "";
        String age = answer3EditText.getText().toString();


        String location = locationTextView.getText().toString();

        Map<String, Object> surveyData = new HashMap<>();
        surveyData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        surveyData.put("favoriteColor", favoriteColor);
        surveyData.put("carType", carType);
        surveyData.put("age", age);
        surveyData.put("location", location);
        surveyData.put("timestamp", System.currentTimeMillis());

        db.collection("surveys")
                .add(surveyData)
                .addOnSuccessListener(documentReference -> {
                    mNotificationHandler.send("Űrlap sikeresen beküldve!");
                    new AlertDialog.Builder(QuestionListActivity.this)
                            .setTitle("Sikeres küldés")
                            .setMessage("Az űrlapot sikeresen elküldted és elmentettük.")
                            .setPositiveButton("OK", null)
                            .show();
                })
                .addOnFailureListener(e -> {
                    new AlertDialog.Builder(QuestionListActivity.this)
                            .setTitle("Hiba")
                            .setMessage("Nem sikerült elküldeni: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                });
    }


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }




    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String currentLocation = location.getLatitude() + ", " + location.getLongitude();
                    locationTextView.setText("Helyzet: " + currentLocation);
                }
            }, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "A helymeghatározás engedélyezése szükséges.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setAlarmManager() {
        long repeatinterval = 60 * 1000;
        long triggerTime = SystemClock.elapsedRealtime() + repeatinterval;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        mManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatinterval,
                pendingIntent);



    }
}
