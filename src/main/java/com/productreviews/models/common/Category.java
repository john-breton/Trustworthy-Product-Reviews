package com.productreviews.models.common;

public enum Category {
    BOOK("Book"), NOT_BOOK("Not Book");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getNameAsString() {
        return name;
    }
}
