package com.productreviews.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    //id of the user
    @Id
    private Long id;

    //name of the user
    private String name;

    //reviews of the user
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    /**
     * create a user
     */
    public User(){
        reviews = new ArrayList<Review>();
    }

    /**
     * create a user with a name
     * @param name
     */
    public User(String name){
        this.name=name;
        reviews = new ArrayList<Review>();
    }

    /**
     * create a user with a name
     * @param name
     */
    public User(String name, Long id){
        this.name=name;
        this.id=id;
        reviews = new ArrayList<Review>();
    }
    /**
     * set the name of the user
     * @param name of the user
     */
    public void setName(String name){
        this.name=name;
    }

    public void setReviews(ArrayList<Review> reviews){
        this.reviews=reviews;
    }

    /**
     * add a new user review
     * @param review that a user made
     */
    public void addReview(Review review){
        reviews.add(review);
    }

    /**
     * get the user reviews
     * @return reviews of the user
     */
    public List<Review> getReviews(){
        return reviews;
    }

    public String getName(){
        return name;
    }

    public Long getID(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

}
