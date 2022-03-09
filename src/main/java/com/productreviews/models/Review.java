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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    /**
     * user that wrote the review
     */
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    /**
     * A username that can be used to key the author of a review.
     */
    private String author; // We could include the actual user object here, but the username should be enough

    /**
     * The score associated with a review, between 0.0 and 5.0
     */
    private int score;

    /**
     * The textual content of a review. This can likely support HTML encodings.
     */
    private String content;

    /**
     * The product a particular review is associated with. A single product can have multiple reviews.
     */
    @ManyToOne(targetEntity = Product.class)
    private Product associatedProduct;

    /**
     * create a review
     */
    public Review(){
    }

    /**
     * create a review with the user, product, rating and comment
     * @param user that wrote the review
     * @param product that the review is for
     * @param rating of the review
     * @param comment of the review
     */
    public Review(User user, Product product, int rating, String comment){
        this.user=user;
        associatedProduct=product;
        score =rating;
        content =comment;
    }

    public Review(Long id){
        this.id=id;
    }

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

    /**
     * set the user of the review
     * @param user that wrote the review
     */
    public void setUser(User user){
        this.user=user;
    }


    public User getUser(){
        return user;
    }

    public Long getUserID(){
        return user.getId();
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
