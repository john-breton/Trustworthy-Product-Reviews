package com.productreviews.Models;

import javax.persistence.*;

@Entity
public class mockReview {

    //if of the review
    @Id
    private Long id;

    //user that wrote the review
    @ManyToOne
    @JoinColumn(name="user_id")
    private mockUser user;

    //review rating
    private int rating;

    //review comment
    private String comment;

    //product of the review
    @ManyToOne
    @JoinColumn(name="product_id")
    private mockProduct product;

    /**
     * create a review
     */
    public mockReview(){
    }

    /**
     * create a review with the user, product, rating and comment
     * @param user that wrote the review
     * @param product that the review is for
     * @param rating of the review
     * @param comment of the review
     */
    public mockReview(mockUser user, mockProduct product, int rating, String comment){
        this.user=user;
        this.product=product;
        this.rating=rating;
        this.comment=comment;
    }

    public mockReview(Long id){
        this.id=id;
    }

    /**
     * set the user of the review
     * @param user that wrote the review
     */
    public void setUser(mockUser user){
        this.user=user;
    }

    /**
     * set the product of the review
     * @param product that the review is for
     */
    public void setProduct(mockProduct product){
        this.product=product;
    }


    /**
     * set the review rating
     * @param rating of the review
     */
    public void setRating(int rating){
        this.rating=rating;
    }

    /**
     * set the comment of the review
     * @param comment of the review
     */
    public void setComment(String comment){
        this.comment=comment;
    }

    public mockUser getUser(){
        return user;
    }

    /**
     * get the name of the user
     * @return the name of the user that wrote the review
     */
    public String getUserName(){
        return user.getName();
    }

    /**
     * get the rating of the review
     * @return rating of the review
     */
    public int getRating(){
        return rating;
    }

    /**
     * get the comment of the review
     * @return comment of the review
     */
    public String getComment(){
        return comment;
    }

    public mockProduct getProduct(){
        return product;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public Long getUserID(){
        return user.getID();
    }
}
