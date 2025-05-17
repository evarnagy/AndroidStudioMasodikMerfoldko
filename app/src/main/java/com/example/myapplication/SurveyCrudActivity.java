package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SurveyCrudActivity extends AppCompatActivity {

    private EditText userIdEditText, favoriteColorEditText, carTypeEditText, ageEditText;
    private Button loadButton, updateButton, deleteButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_crud);

        userIdEditText = findViewById(R.id.userIdEditText);
        favoriteColorEditText = findViewById(R.id.favoriteColorEditText);
        carTypeEditText = findViewById(R.id.carTypeEditText);
        ageEditText = findViewById(R.id.ageEditText);

        loadButton = findViewById(R.id.loadButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        Button cancelButton = findViewById(R.id.cancel_button);

        db = FirebaseFirestore.getInstance();

        loadButton.setOnClickListener(v -> loadSurvey());
        updateButton.setOnClickListener(v -> updateSurvey());
        deleteButton.setOnClickListener(v -> deleteSurvey());
        cancelButton.setOnClickListener(view -> finish());
    }

    private void loadSurvey() {
        String userId = userIdEditText.getText().toString();

        db.collection("surveys")
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        var document = queryDocumentSnapshots.getDocuments().get(0);
                        favoriteColorEditText.setText(document.getString("favoriteColor"));
                        carTypeEditText.setText(document.getString("carType"));
                        ageEditText.setText(document.getString("age"));
                        Toast.makeText(this, "Adatok betöltve!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Nincs adat ezzel a userId-val!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Hiba történt: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateSurvey() {
        String userId = userIdEditText.getText().toString();

        db.collection("surveys")
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        var document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();

                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("favoriteColor", favoriteColorEditText.getText().toString());
                        updatedData.put("carType", carTypeEditText.getText().toString());
                        updatedData.put("age", ageEditText.getText().toString());

                        docRef.update(updatedData)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Sikeres frissítés!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Hiba frissítésnél: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Nincs adat frissítéshez!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteSurvey() {
        String userId = userIdEditText.getText().toString();

        db.collection("surveys")
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        var document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();

                        docRef.delete()
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Adat törölve!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Hiba törlésnél: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Nincs adat törléshez!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
