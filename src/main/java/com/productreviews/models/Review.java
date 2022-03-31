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
     * A unique ID for the Review object for persistence purposes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The user that wrote the review
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The score associated with a review, between 0.0 and 5.0
     */
    private int score;

    /**
     * The textual content of a review. This can likely support HTML encodings
     */
    private String content;

    /**
     * The product a particular review is associated with. A single product can have multiple reviews
     */
    @ManyToOne(targetEntity = Product.class)
    private Product associatedProduct;

    /**
     * Create an empty review.
     */
    public Review() {
    }

    /**
     * Create a review with the user, product, rating, and comment
     *
     * @param user    The user that wrote the review
     * @param product The product that the review is for
     * @param rating  The rating of the review
     * @param comment The textual comment of the review
     */
    public Review(User user, Product product, int rating, String comment) {
        this.user = user;
        associatedProduct = product;
        score = rating;
        content = comment;
    }

    public Review(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Product getAssociatedProduct() {
        return associatedProduct;
    }

    public void setAssociatedProduct(Product associatedProduct) {
        this.associatedProduct = associatedProduct;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserID() {
        return user.getId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", score=" + score +
                ", content='" + content + '\'' +
                '}';
    }

}
