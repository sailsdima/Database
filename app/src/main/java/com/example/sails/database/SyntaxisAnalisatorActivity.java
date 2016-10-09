package com.example.sails.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class SyntaxisAnalisatorActivity extends AppCompatActivity {

    TextView textViewSyntAnalisatorAnalis, textViewSyntAnalisatorQuery;
    String LOG_TAG = "dbLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syntaxis_analisator);

        textViewSyntAnalisatorAnalis = (TextView) findViewById(R.id.textViewSyntAnalisatorAnalis);
        textViewSyntAnalisatorQuery = (TextView) findViewById(R.id.textViewSyntAnalisatorQuery);

        String query = getIntent().getStringExtra("query");

        Log.d(LOG_TAG, "Before creating CheckActivity");

        textViewSyntAnalisatorQuery.setText(query);
        CheckActivity checkActivity = new CheckActivity();
        ArrayList<Lexeme> lexemes = checkActivity.parseQuery(query);
        Log.d(LOG_TAG, lexemes.get(1).toString());

    }
}
