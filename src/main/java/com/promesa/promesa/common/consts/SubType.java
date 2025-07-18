package com.promesa.promesa.common.consts;

public enum SubType {
    MAIN("main"),
    DETAIL("detail"),
    PROFILE("profile"),
    REVIEW("review"),
    THUMBNAIL("thumbnail"),
    PROMOTION("promotion");

    private final String path;
    SubType(String path) { this.path = path; }
    public String getPath() { return path; }
}
