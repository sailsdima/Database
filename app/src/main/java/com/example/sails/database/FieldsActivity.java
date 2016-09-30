package com.example.sails.database;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FieldsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText1, editText2, editText3, editText4;
    TextView textView1, textView2, textView3, textView4;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields);

        Log.d(DBHelperUniv.LOG_TAG, "class FieldsActivyty, method onCreate.");

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        btnAdd = (Button) findViewById(R.id.btnFieldsAdd);
        btnCancel = (Button) findViewById(R.id.btnFieldsCancsel);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();

        setTitle(intent.getStringExtra("table"));

        switch (intent.getStringExtra("table")){
            case DBHelperUniv.TABLE_VYKLADACH:
                textView1.setText(DBHelperUniv.TABLE_VYKLADACH_KODVYKL);
                textView2.setText(DBHelperUniv.TABLE_VYKLADACH_PRIZVVYKL);
                textView3.setText(DBHelperUniv.TABLE_VYKLADACH_KODPOST);
                textView4.setText(DBHelperUniv.TABLE_VYKLADACH_NOMKAF);

                editText4.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);


                Log.d(DBHelperUniv.LOG_TAG, "class FieldsActivyty, method onCreate. TABLE = VYKLADACH");

                break;

            case DBHelperUniv.TABLE_POSADA:

                textView1.setText(DBHelperUniv.TABLE_POSADA_KODPOST);
                textView2.setText(DBHelperUniv.TABLE_POSADA_POSTVYKL);
                textView3.setText(DBHelperUniv.TABLE_POSADA_NORMPOST);

                editText4.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);

                Log.d(DBHelperUniv.LOG_TAG, "class FieldsActivyty, method onCreate. TABLE = POSADA");

                break;

        }
    }

    @Override
    public void onClick(View view) {

        Log.d("dbLogs", "methodOnclick fieldsActivity.");

        switch (view.getId()){
            case R.id.btnFieldsAdd:

                switch (getIntent().getStringExtra("table")){
                    case DBHelperUniv.TABLE_VYKLADACH:
                        tableVykladachRequest();
                        break;
                    case DBHelperUniv.TABLE_POSADA:
                        tablePosadaRequest();
                        break;
                }


                break;

            case R.id.btnFieldsCancsel:
                finish();
                break;


        }
    }


    private void tablePosadaRequest(){

        Log.d(DBHelperUniv.LOG_TAG, "class FieldsActivyty, method tablePosadaRequest.");

        Intent intent = new Intent();
        intent.putExtra("kodPost",editText1.getText().toString());
        intent.putExtra("postVykl",editText2.getText().toString());
        intent.putExtra("normPost",editText3.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void tableVykladachRequest(){
        Log.d(DBHelperUniv.LOG_TAG, "class FieldsActivyty, method tableVykladachRequest.");

        Intent intent = new Intent();
        intent.putExtra("kodVykl",editText1.getText().toString());
        intent.putExtra("prizvVykl",editText2.getText().toString());
        intent.putExtra("kodPost",editText3.getText().toString());
        intent.putExtra("nomKaf",editText4.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}

