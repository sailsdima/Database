package com.example.sails.database;

import android.graphics.Color;
import android.print.PrintAttributes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CheckActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayList<Token> myTokens;
    private String arrSymbols[];
    private String arrComparisonOperators[];
    private String arrSymbolsQuotes[];
    private String strInsteadSpace = "#32";
    private final static String LOG_TAG = "dbLogs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);



        TextView editTextQuery = (TextView) findViewById(R.id.textViewCheckActivityQuery);
        listView = (ListView) findViewById(R.id.listViewCheckActivity);

        initMyTokens();

        String query = getIntent().getStringExtra("query");
        editTextQuery.setText(query);
        parseQuery(query);
    }


    private void initMyTokens(){
        myTokens = new ArrayList<>();

        String arrKeywords[] = getResources().getStringArray(R.array.keyWords);
        String arrDataTypes[] = getResources().getStringArray(R.array.dataTypes);
        arrComparisonOperators = getResources().getStringArray(R.array.comparisonOperators);
        String arrCompoundOperator[] = getResources().getStringArray(R.array.compoundOperator);

        arrSymbols = getResources().getStringArray(R.array.symbols);
        arrSymbolsQuotes = getResources().getStringArray(R.array.symbolsQuotes);
        Pattern patternNumbers = Pattern.compile("[0-9]*");
        Pattern patternColumns = Pattern.compile("([a-zA-Z])([a-zA-Z]|[0-9]|_|-){0,8}");
        Pattern patternTables = Pattern.compile("([a-zA-Z]|_)([a-zA-Z]|[0-9]|_|-){0,15}");
        Pattern patternConstValues = Pattern.compile("\'.*\'|\".*\"");

        myTokens.add(new Token("keyWord", arrKeywords));
        myTokens.add(new Token("keyWord (dataType)", arrDataTypes));
        myTokens.add(new Token("number", patternNumbers));
        myTokens.add(new Token("compoun dOperator", arrCompoundOperator));
        myTokens.add(new Token("comparison operator", arrComparisonOperators));
        myTokens.add(new Token("symbol quote", arrSymbolsQuotes));
        myTokens.add(new Token("symbol", arrSymbols));
        myTokens.add(new Token("constant value", patternConstValues));
        myTokens.add(new Token("table or column", patternColumns));
        myTokens.add(new Token("column", patternTables));
        myTokens.add(new Token("unknown token", Pattern.compile(".*")));
    }

    private void parseQuery(String query) {
        Log.d(LOG_TAG, "class CustomQueryActivity. Method parseQuery(query). query = \n" + query);

        for(Token t: myTokens) {
            Log.d(LOG_TAG, t.getMask());
        }


        String [] words = splitLine(query.replaceAll("[\\s]{2,}", " ").trim());

        HashMap<String, String> map;
        ArrayList<HashMap<String,String>> arrayData = new ArrayList<>();

        for (String word: words) {
            for(Token t: myTokens){
                if(t.checkMask(word)){
                    if(t.getName().equals("constant value"))
                    {
                        word = word.replaceAll(strInsteadSpace, " ");
                    }
                    map = new HashMap<>();
                    map.put("word", word);
                    map.put("type", t.getName());
                    map.put("begins", String.valueOf(word.charAt(0)));
                    map.put("length", String.valueOf(word.length()));
                    arrayData.add(map);
                    Log.d(LOG_TAG, "Word \"" + word + "\" \t\tType: " + t.getName() + "\t\tBegins: \"" + word.charAt(0) + "\"" + "\t\tLength: " + word.length());
                    break;
                }
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, arrayData, R.layout.four_text_views,
                new String[]{"word", "type", "begins", "length"},
                new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4});

        LayoutInflater layoutInflater = getLayoutInflater();
        View headerView = layoutInflater.inflate(R.layout.four_text_views_header, null, false);
        TextView textView1 = (TextView) headerView.findViewById(R.id.text1);
        textView1.setText("Word");
        TextView textView2 = (TextView) headerView.findViewById(R.id.text2);
        textView2.setText("Type");
        TextView textView3 = (TextView) headerView.findViewById(R.id.text3);
        textView3.setText("Begins");
        TextView textView4 = (TextView) headerView.findViewById(R.id.text4);
        textView4.setText("Length");

        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);

    }

    private String[] splitLine(String line){

        Log.d(LOG_TAG, "class CustomQueryActivity. Method splitLine(line). line = \n" + line);

        StringBuilder sLine = new StringBuilder(line);
        sLine.insert(sLine.length()," ");


        for(int i = 0; i < sLine.length(); i++){
            if(masContains(arrSymbolsQuotes, sLine.charAt(i))) {
                char quoteSymb = sLine.charAt(i);
                Log.d(LOG_TAG, sLine.charAt(i) + "");

                i++;

                while (sLine.charAt(i) != quoteSymb) {
                    Log.d(LOG_TAG, sLine.charAt(i) + "");
                    if (sLine.charAt(i) == ' ') {
                        sLine.replace(i, i + 1, strInsteadSpace);
                        i += strInsteadSpace.length() - 1;

                    }
                    i++;
                }
                i++;
            }

            if(masContains(arrSymbols, sLine.charAt(i)) || masContains(arrSymbolsQuotes, sLine.charAt(i))){
                if (sLine.charAt(i - 1) != ' ' && !masContains(arrSymbols, sLine.charAt(i-1))) {
                    sLine.insert(i, " ");
                    i++;
                }

                if (((!masContains(arrSymbols, sLine.charAt(i+1)) && (sLine.charAt(i + 1) != ' ')) || masContains(arrSymbolsQuotes, sLine.charAt(i+1))) && (sLine.charAt(i + 1) < sLine.length())) {
                    sLine.insert(i + 1, " ");
                }
            }
        }


        Log.d(LOG_TAG, "Before Spliting. line = \n" + sLine);
        return new String(sLine).split(" ");
    }


    public boolean masContains(String [] mas, char value){

        if (null == mas){
            Log.d(LOG_TAG, "method masContains() mas == null.");
            return false;
        }

        for(int i = 0; i < mas.length; i++){
            if(mas[i].charAt(0) == value)
                return true;
        }
        return false;
    }

    public boolean masContains(String [] mas, String value){
        Log.d(LOG_TAG,"MASCONTAINS " + value);

        if (null == mas){
            Log.d(LOG_TAG, "method masContains() mas == null.");
            return false;
        }

        for(int i = 0; i < mas.length; i++){
            if(mas[i].equals(value))
                return true;
        }
        return false;
    }
}
