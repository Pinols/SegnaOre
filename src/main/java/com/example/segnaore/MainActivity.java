package com.example.segnaore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                try
                {
                    this.getSupportActionBar().hide();
                }
                catch (NullPointerException e){}
                setContentView(R.layout.activity_main);
                Button apri = findViewById(R.id.B001);
                apri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myint= new Intent(view.getContext(),MainView.class);
                        startActivity(myint);
                    }
                });
    }
}