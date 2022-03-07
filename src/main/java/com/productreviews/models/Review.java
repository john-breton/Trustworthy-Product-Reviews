package com.productreviews.models;


import javax.persistence.*;

/**
 * Review represents a minimal entity that has an author
 * username for identification purposes, a score to
 * show the rating associated with the review, and
 * the actual review text. A review can be associated with
 * a single user, but a user can make multiple reviews. Likewise,
 * a review can be associated with a single Product, but a Product
 * can have multiple reviews associated with it.
 */
@Entity
public class Review {

    /**
     * A unique ID for the Review object for persistence purposes.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * A username that can be used to key the author of a review.
     */
    private String author; // We could include the actual user object here, but the username should be enough

    /**
     * The product a particular review is associated with. A single product can have multiple reviews.
     */
    @OneToOne(targetEntity = Product.class)
    private Product associatedProduct;

    /**
     * The score associated with a review, between 0.0 and 5.0
     */
    private float score;

    /**
     * The textual content of a review. This can likely support HTML encodings.
     */
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authorUsername) {
        this.author = authorUsername;
    }

    public Product getAssociatedProduct() {
        return associatedProduct;
    }

    public void setAssociatedProduct(Product associatedProduct) {
        this.associatedProduct = associatedProduct;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", associatedProduct=" + associatedProduct +
                ", score=" + score +
                ", content='" + content + '\'' +
                '}';
    }
}
