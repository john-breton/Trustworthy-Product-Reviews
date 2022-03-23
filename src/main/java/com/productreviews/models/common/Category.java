package com.productreviews.models.common;

/**
 * The Category enum represents the possible categories a product can
 * be included in. This is useful for sorting products by a specific
 * category, such as products that are books and those that are not.
 */
public enum Category {
    BOOK("Book"), NOT_BOOK("Not Book"), FURNITURE("Furniture");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
