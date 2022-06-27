package com.example.epmemi;

public class ListData {
    private String text;
    private String filename;

    public ListData(String text, String filename) {
        this.text = text;
        this.filename = filename;
    }

    public String getText(){
        return this.text;
    }
    public String getFileName() { return this.filename; }
}
