package kz.informatics.okulik.nalog_app.home.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
            Uri uri = Uri.parse("geo:43.238949,76.889709?q=43.238949,76.889709(Shymbulak Resort)");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

    }
}