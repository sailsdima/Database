package com.example.sails.database;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckActivity extends AppCompatActivity {

    private ListView listView;

    private String arrSymbols[];
    private String arrComparisonOperators[];
    private String arrSymbolsQuotes[];
    private String strInsteadSpace = "#32";
    private final static String LOG_TAG = "dbLogs";
    private SimpleAdapter adapter;
    private boolean adapterWasSet = false;

    LexicalAnalisator lexicalAnalisator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        final EditText editTextQuery = (EditText) findViewById(R.id.textViewCheckActivityQuery);
        listView = (ListView) findViewById(R.id.listViewCheckActivity);

        lexicalAnalisator = new LexicalAnalisator(this);

        String query = getIntent().getStringExtra("query");
        editTextQuery.setText(query);


        drawLexemsTable(lexicalAnalisator.getLexemes(query));

        editTextQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        Log.d(LOG_TAG, "SPACE CLICKED");

                        drawLexemsTable(lexicalAnalisator.getLexemes(editTextQuery.getText().toString()));

                        return true;
                    }
                }
                return false;
            }
        });
    }


    private void drawLexemsTable(ArrayList<Lexeme> lexemes) {


        for (Lexeme lexeme: lexemes) {
            Log.d(LOG_TAG, "AAAAAAA   " + lexeme.toString());
        }

        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> arrayData = new ArrayList<>();

        for (Lexeme lexeme : lexemes) {
            map = new HashMap<>();
            map.put("word", lexeme.getWord());
            map.put("type", lexeme.getType());
            map.put("position", String.valueOf(lexeme.getPosition()));
            map.put("begins", String.valueOf(lexeme.getBeginsFrom()));
            map.put("length", String.valueOf(lexeme.getLength()));
            arrayData.add(map);
            Log.d(LOG_TAG, lexeme.toString());
        }

        if (!adapterWasSet)
            createDialog(arrayData);
        else
            adapter.notifyDataSetChanged();

    }

    private void createDialog(ArrayList<HashMap<String, String>> arrayData) {
        adapterWasSet = true;

        adapter = new SimpleAdapter(this, arrayData, R.layout.two_text_views,
                new String[]{"word", "type"},
                new int[]{R.id.text1, R.id.text2});

        LayoutInflater layoutInflater = getLayoutInflater();
        View headerView = layoutInflater.inflate(R.layout.two_text_views_header, null, false);
        TextView textView1 = (TextView) headerView.findViewById(R.id.text1);
        textView1.setText("Word");
        TextView textView2 = (TextView) headerView.findViewById(R.id.text2);
        textView2.setText("Type");

        if (listView.getHeaderViewsCount() < 1)
            listView.addHeaderView(headerView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(i);

                String word = hashMap.get("word");
                String type = hashMap.get("type");
                String beginsFrom = hashMap.get("begins");
                String position = hashMap.get("position");
                String length = hashMap.get("length");

                Dialog dialog = new Dialog(CheckActivity.this);
                dialog.setContentView(R.layout.dialog_info_layout);
                dialog.setTitle("Word analysis");

                ((TextView) dialog.findViewById(R.id.textViewDialogWord)).setText(word);
                ((TextView) dialog.findViewById(R.id.textViewDialogType)).setText(type);
                ((TextView) dialog.findViewById(R.id.textViewDialogBegins)).setText(beginsFrom);
                ((TextView) dialog.findViewById(R.id.textViewDialogPosition)).setText(position);
                ((TextView) dialog.findViewById(R.id.textViewDialogLength)).setText(length);

                dialog.show();
            }
        });
    }

}
