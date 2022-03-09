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
     * A unique ID for the User object for persistence purposes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * A unique username used to represent the user's identity
     */
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    /**
     * A password associated with this user's account
     */
    @Column(nullable = false, length = 64)
    private String password;

    /**
     * A list contained the users a particular user is following
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = User.class)
    private List<User> followingList;

    /**
     * A list of reviews the user has created for products in the system
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    /**
     * Create a new empty user
     */
    public User() {
    }

    /**
     * Create a new user with their login credentials
     *
     * @param username The username for the user, which should be unique
     * @param password The password for the user that will be authenticated on login
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Create a new base user with a username, but no password
     *
     * @param username The username for the user, which should be unique
     */
    public User(String username) {
        this.username = username;

    }

    /**
     * Create a new base user with a username and specific id, but no password
     *
     * @param username The username for the user, which should be unique
     * @param id       The specific ID that will be assigned to the user
     */
    public User(String username, Long id) {
        this.username = username;
        this.id = id;
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

    /**
     * Add a new follower to the list of users this user is following
     *
     * @param following The new user to be added to the following list, should they not already exist
     */
    public void addFollowing(User following) {
        // We might want to consider a hash map to have Jaccard distance info
        // ready for lookup. Extra argument would be the distance.
        followingList.add(following);
    }

    /**
     * This method allows another user to unfollow this user
     * @param follower the follower that wants to unfollow
     */
    public void unFollow(User follower){
        this.followingList.remove(follower);
    }

    public void setFollowingList(List<User> followingList) {
        this.followingList = followingList;
    }

    public List<User> getFollowingList() {
        return followingList;
    }


    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Add a new user review to the list of reviews this user has created
     *
     * @param review The review that a user made
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    /**
     * Get the reviews a user has created
     *
     * @return A List of reviews the user has created
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Check if the user is following a specified user
     *
     * @param following The target user that we wish to determine if this user is following
     * @return True if the user is following the target, false otherwise
     */
    public boolean isFollowing(User following) {
        return followingList.stream().anyMatch(user -> user.equals(following));
    }

    /**
     * A method to return the Jaccard distance between this and another user
     * @param otherUser
     * @return
     */
    public int getJaccardDistance(User otherUser){
        List<User> followerFollowingList = otherUser.getFollowingList();
        // TO DO: calculate jaccard
        return 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", followingList=" + followingList +
                ", reviews=" + reviews +
                '}';
    }

}
