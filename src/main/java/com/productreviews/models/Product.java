package com.productreviews.models;

import javax.persistence.*;

/**
 * Product represents a minimal entity that has a name,
 * textual description, and an image reference. The image
 * itself is not persisted, but the href link is maintained.
 */
@Entity
public class Product {

    /**
     * A unique ID for the Product object for persistence purposes.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * The name of the product. This can likely support HTML encodings.
     */
    @Column(unique = true)
    private String name;

    /**
     * The textual description of a product. This can likely support HTML encodings.
     */
    private String description; // This might not be strictly necessary to include in the app.

    /**
     * An HTML reference to an image that is associated with this product.
     */
    private String hrefImage;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHrefImage() {
        return hrefImage;
    }

    public void setHrefImage(String hrefImage) {
        this.hrefImage = hrefImage;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hrefImage='" + hrefImage + '\'' +
                '}';
    }
}
