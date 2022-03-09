package com.productreviews.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User represents a minimal entity that has a username and
 * a list of users that this user is following. Based on
 * Jaccard distance, the closer another user is to being
 * followed by a given user, the more trustworthy their
 * review will be.
 */
@Entity
public class User {

    /**
     * A unique ID for the User object for persistence purposes.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * A unique username used to represent the user's identity.
     */
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    /**
     * A password associated with this user's account.
     */
    @Column(nullable = false, length = 64)
    private String password;

    /**
     * A list contained the users a particular user is following.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = User.class)
    private List<User> followingList;

    //reviews of the user
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * REMOVE LATER
     * @param name
     */
    public User(String name){
        this.username =name;

    }

    /**
     * REMOVE LATER
     * @param name
     */
    public User(String name, Long id){
        this.username =name;
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFollowing(User following) {
        // We might want to consider a hash map to have Jaccard distance info
        // ready for lookup. Extra argument would be the distance.
        followingList.add(following);
    }

    public void setFollowingList(List<User> followingList){
        this.followingList=followingList;
    }

    public List<User> getFollowingList() {
        return followingList;
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

    /**
     * check if the user is following a specified user
     * @param following
     * @return
     */
    public boolean isFollowing(User following){
        for(int i=0; i<followingList.size(); i++){
            if(followingList.get(i).getUsername().equals(following.getUsername())){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", followingList=" + followingList +
                '}';
    }

}
