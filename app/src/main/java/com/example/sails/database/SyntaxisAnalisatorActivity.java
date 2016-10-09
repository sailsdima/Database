package com.example.sails.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SyntaxisAnalisatorActivity extends AppCompatActivity {

    TextView textViewSyntAnalisatorAnalis, textViewSyntAnalisatorQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syntaxis_analisator);

        textViewSyntAnalisatorAnalis = (TextView) findViewById(R.id.textViewSyntAnalisatorAnalis);
        textViewSyntAnalisatorQuery = (TextView) findViewById(R.id.textViewSyntAnalisatorQuery);

        textViewSyntAnalisatorQuery.setText(getIntent().getStringExtra("query"));
        CheckActivity checkActivity = new CheckActivity();

    }
}
