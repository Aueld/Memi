package com.example.epmemi;

public class ListDataDel {
    private String text;
    private String filename;

    public ListDataDel(String text, String filename) {
        this.text = text;
        this.filename = filename;
    }

    public String getText(){
        return this.text;
    }
    public String getFileName() { return this.filename; }
}
