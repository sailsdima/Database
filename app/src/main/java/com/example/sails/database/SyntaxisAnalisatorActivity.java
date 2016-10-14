package com.example.sails.database;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ImageView;
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

        LexicalAnalisator lexicalAnalisator = new LexicalAnalisator(this);

        String zapros1 = "SELECT <column names> FROM <column name> INNER JOIN <column name> ON <column names> = <column names>" +
                " WHERE <column names> " +
                "= <constant value> AND <column names> = <constant value>;";


        ArrayList<Lexeme> lexemes = lexicalAnalisator.getLexemes(query);
       /* StringBuilder stringBuilder = new StringBuilder();
        for(Lexeme lexeme : lexemes){
            if(lexeme.getType().equals("keyWord") || lexeme.getType().equals("comparison operators")
                    || lexeme.getType().equals("compound Operator") || lexeme.getType().equals("symbol"))
                stringBuilder.append(lexeme.getWord() + " ");
            else
                stringBuilder.append("<" + lexeme.getType() + "> ");
        }*/
// Чистый код Мартина


        StringBuilder stringQuery = new StringBuilder();
        for(int i = 0; i < lexemes.size(); i++){
            if(lexemes.get(i).getType().equals("table or column"))
                stringQuery.append("<column name>");
            else  if (lexemes.get(i).getType().equals("constant value"))
                stringQuery.append("<constant value>");
            else if(lexemes.get(i).getType().equals("symbol"))
                stringQuery.append(lexemes.get(i).getWord());
            else stringQuery.append(" " + lexemes.get(i).getWord() + " ");
        }


        Log.d(LOG_TAG, "00000 " + query);
        Log.d(LOG_TAG, "11111 " + stringQuery.toString());
        String changedQuery = stringQuery.toString().replaceAll("(<column name>.<column name>,?)+", " <column names> ");
        changedQuery = changedQuery.trim();
        changedQuery = changedQuery.replaceAll("[\\s]{2,}", " ");
        Log.d(LOG_TAG, "22222 " + changedQuery);

 
        ImageView imageView = (ImageView) findViewById(R.id.imageViewSyntaxisAnalisator);
        if(changedQuery.equals(zapros1)){
            imageView.setImageResource(android.R.drawable.arrow_up_float);
            imageView.setBackgroundColor(Color.GREEN);
            textViewSyntAnalisatorAnalis.setText("success!");
        }

        else {
            imageView.setImageResource(android.R.drawable.arrow_down_float);
            imageView.setBackgroundColor(Color.RED);
            textViewSyntAnalisatorAnalis.setText("no success!");
        }

        Log.d(LOG_TAG, changedQuery);
        Log.d(LOG_TAG, zapros1);
        textViewSyntAnalisatorAnalis.append(changedQuery);
        //Log.d(LOG_TAG, lexemes.get(1).toString());

    }
}
