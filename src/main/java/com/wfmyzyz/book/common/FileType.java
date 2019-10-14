package com.wfmyzyz.book.common;

public enum  FileType {
    JPG("jpg"),
    PNG("png"),
    SVG("svg"),
    GIF("gif"),
    PDF("pdf");

    FileType(String type) {
        this.type = type;
    }

    private String type;

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }
}
