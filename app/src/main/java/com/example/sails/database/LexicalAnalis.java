package com.example.sails.database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by sails on 09.10.2016.
 */

public class LexicalAnalis {
    private Context context;
    private ArrayList<Token> myTokens;
    private String arrSymbols[];
    private String arrComparisonOperators[];
    private String arrSymbolsQuotes[];
    private String strInsteadSpace = "#32";
    private final static String LOG_TAG = "dbLogs";

    LexicalAnalis(Context context){
        this.context = context;
        initMyTokens();
        Log.d(LOG_TAG,"LexicalAnalis class created.");
    }

    private void initMyTokens(){
        myTokens = new ArrayList<>();

        String arrKeywords[] = context.getResources().getStringArray(R.array.keyWords);
        String arrDataTypes[] = context.getResources().getStringArray(R.array.dataTypes);
        arrComparisonOperators = context.getResources().getStringArray(R.array.comparisonOperators);
        String arrCompoundOperator[] = context.getResources().getStringArray(R.array.compoundOperator);

        arrSymbols = context.getResources().getStringArray(R.array.symbols);
        arrSymbolsQuotes = context.getResources().getStringArray(R.array.symbolsQuotes);
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


    public ArrayList<Lexeme> parseQuery(String query) {
        Log.d(LOG_TAG, "class LexicalAnalis. Method parseQuery(query). query = \n" + query);

        for(Token t: myTokens) {
            Log.d(LOG_TAG, t.getMask());
        }

        //String [] words = splitLine(query.replaceAll("[\\s]{2,}", " ").trim());
        ArrayList<Lexeme> lexemes = splitLine(query.replaceAll("[\\s]{2,}", " ").trim());

        for (Lexeme lexeme: lexemes) {
            for(Token t: myTokens){
                if(t.checkMask(lexeme.getWord())){
                    if(t.getName().equals("constant value"))
                    {
                        lexeme.setWord(lexeme.getWord().replaceAll(strInsteadSpace, " "));
                    }
                    lexeme.setType(t.getName());

                    break;
                }
            }
        }

        return lexemes;
    }

    private ArrayList<Lexeme> splitLine(String line){

        Log.d(LOG_TAG, "class LexicalAnalis. Method splitLine(line). line = \n" + line);

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

        String [] words = new String(sLine).split(" ");
        ArrayList<Lexeme> arrayLexemes = new ArrayList<>();
        int fromIndex = 0;
        int index = 0;

        for( int i = 0; i < words.length; i++){
            if ((index = line.indexOf(words[i], fromIndex)) > -1){
                arrayLexemes.add(new Lexeme(words[i], index));
                fromIndex += words[i].length();
            }
        }
        return arrayLexemes;
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
}
