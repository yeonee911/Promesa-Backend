package com.promesa.promesa.common.consts;

public enum ImageType {
    ITEM("item"),
    ARTIST("artist"),
    MEMBER("member"),
    EXHIBITION("exhibition");

    private final String path;
    ImageType(String path) { this.path = path; }
    public String getPath() { return path; }
}