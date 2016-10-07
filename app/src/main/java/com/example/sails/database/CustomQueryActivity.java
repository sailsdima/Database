package com.example.sails.database;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CustomQueryActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    Button buttonCustomCancel, buttonCustomDoQuery, buttonCustomCheck, buttonCustomKeyWords, buttonCustomTables;
    EditText editTextCustomQuery;

    private final static String LOG_TAG = "dbLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_query);

        buttonCustomCancel = (Button) findViewById(R.id.buttonCustomCancel);
        buttonCustomDoQuery = (Button) findViewById(R.id.buttonCustomDoQuery);
        buttonCustomCheck = (Button) findViewById(R.id.buttonCustomCheck);
        buttonCustomKeyWords = (Button) findViewById(R.id.buttonCustomKeyWords);
        buttonCustomTables = (Button) findViewById(R.id.buttonCustomTables);

        buttonCustomCancel.setOnClickListener(this);
        buttonCustomDoQuery.setOnClickListener(this);
        buttonCustomCheck.setOnClickListener(this);
        buttonCustomKeyWords.setOnClickListener(this);
        buttonCustomTables.setOnClickListener(this);

        buttonCustomKeyWords.setOnLongClickListener(this);

        editTextCustomQuery = (EditText) findViewById(R.id.editTextCustomQuery);


    }

    @Override
    public boolean onLongClick(View view) {

        switch (view.getId()) {
            case R.id.buttonCustomKeyWords:
                keyWordsInsertion(getResources().getStringArray(R.array.keyWords));
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonCustomDoQuery:
                Intent intent = new Intent();
                intent.putExtra("query", editTextCustomQuery.getText().toString());
                Log.d("dbLogs", "CustomQueryActivity. onClick(). Button DoQuery" + intent.getStringExtra("query"));
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.buttonCustomCancel:
                Log.d("dbLogs", "CustomQueryActivity. onClick(). Button Cancel" );
                finish();
                break;

            case R.id.buttonCustomCheck:
                Log.d("dbLogs", "CustomQueryActivity. onClick(). Button Check ");

                Intent intent1 = new Intent(this, CheckActivity.class);
                intent1.putExtra("query", editTextCustomQuery.getText().toString());
                startActivity(intent1);

                break;

            case R.id.buttonCustomKeyWords:
                keyWordsInsertion(getResources().getStringArray(R.array.commonWords));
                break;

            case R.id.buttonCustomTables:
                SQLiteDatabase database = UniverDBActivity.getDataBase();

                ArrayList<String> arrColumns = new ArrayList<>();
                String tableNames[] = {DBHelperUniv.TABLE_POSADA, DBHelperUniv.TABLE_VYKLADACH};

                for (String table: tableNames) {

                    Cursor c = database.rawQuery("SELECT * FROM " + table, null);

                    arrColumns.add(table);
                    for(int j = 0; j < c.getColumnCount(); j++){
                        arrColumns.add(table + "." + c.getColumnName(j));
                    }

                    c.close();
                }
                keyWordsInsertion(arrColumns.toArray());
                break;

        }
    }

    private void keyWordsInsertion(final Object s1[]){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String s[] = new String[s1.length];
        for(int i = 0; i < s1.length; i++)
            s[i] = (String)s1[i];

        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = editTextCustomQuery.getSelectionStart();
                StringBuilder text = new StringBuilder(editTextCustomQuery.getText().toString());
                text.insert(position, s[i] + " ");

                if((position > 1) && (editTextCustomQuery.getText().charAt(position - 1) != ' ')) {
                    text.insert(position, " ");
                    position++;
                }
                position++;

                editTextCustomQuery.setText(text.toString());
                editTextCustomQuery.setSelection(position + s[i].length());

            }
        });
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

    }



}
