package kz.informatics.okulik.nalog_app.home.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import kz.informatics.okulik.R;

public class OneOtelActivity extends AppCompatActivity {

    Button bronButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_otel);

        bronButton = findViewById(R.id.bronButton);
        bronButton.setOnClickListener(view -> {
            Toast.makeText(this, "Бронь прошла успешно!!!", Toast.LENGTH_SHORT).show();
        });

    }
}