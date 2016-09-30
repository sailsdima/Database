package com.example.sails.database;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sails on 19.09.2016.
 */
public class Token {
    final static String LOG_TAG = "dbLogs";
    private String name;
    private Pattern mask;

    Token(){
        Log.d(LOG_TAG, "Class Token:: Token created.");
    }

    Token(String name, String [] values){
        this.name = name;
        createMask(values);

        Log.d(LOG_TAG, "Class Token::Token created. Name: " + name + "\nMask: " + mask);
    }

    Token(String name, Pattern mask){
        this.name = name;
        this.mask = mask;


        Log.d(LOG_TAG, "Class Token:: Token created. Name: " + name + "\nMask: " + mask);
    }

    public boolean checkMask(String word){
        Matcher matcher = mask.matcher(word);
        if(matcher.matches())
            return true;
        return false;
    }

    private void createMask(String [] values){
        StringBuilder patt = new StringBuilder();
        Log.d(LOG_TAG, "Method called");

        for (String v: values)
        {
            StringBuffer s = new StringBuffer(v);
            for(int i = 0; i < v.length(); i++) {
                if ((s.charAt(i) == '+') || (s.charAt(i) == '.') || (s.charAt(i) == '*') || (s.charAt(i) == '^') || (s.charAt(i) == '"')) {

                    Log.d(LOG_TAG, name + "  Symbol");
                    s.insert(i, "\\");
                    i++;
                }
            }
            patt.append(s.toString().trim() + "|");
        }

        patt.deleteCharAt(patt.length() - 1);

        this.mask = Pattern.compile(patt.toString());


        Log.d(LOG_TAG, "Class Token:: Method createMask worked. Name: " + name + "\nNew mask: " + mask);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMask(String [] values){
        createMask(values);
    }

    public String getMask(){
        return mask.toString();
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name + "\n" + mask.toString();
    }
}