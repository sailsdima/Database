package com.example.sails.database;

/**
 * Created by sails on 30.09.2016.
 */
public class Lexeme {
    private String word;
    private String type = "";
    private int position;
    private int length;
    private char beginsFrom;


    public Lexeme(String word, int position) {
        this.word = word;
        this.position = position;
        this.length = word.length();
        this.beginsFrom = word.charAt(0);
    }

    public void setType(String type){
        this.type = type;
    }

    public void setWord(String word) {
        this.word = word;
        this.length = word.length();
        this.beginsFrom = word.charAt(0);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public char getBeginsFrom() {
        return beginsFrom;
    }

    public String getType(){
        return type;
    }

    public String toString(){
        return  "Word \"" + word + "\" \t\tType: " + type + "\t\tPosition: \"" + position + "\"" + "\t\tBeginsFrom " + beginsFrom + "\t\tLength: " + length;
    }

}
