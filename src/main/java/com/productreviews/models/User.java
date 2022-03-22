package com.productreviews.models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = User.class)
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
        followingList = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    /**
     * Create a new base user with a username, but no password
     *
     * @param username The username for the user, which should be unique
     */
    public User(String username) {
        this.username = username;
        followingList = new ArrayList<>();
        reviews = new ArrayList<>();
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
        followingList = new ArrayList<>();
        reviews = new ArrayList<>();
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
        followingList = new ArrayList<>();
        reviews = new ArrayList<>();
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

    public void setFollowingList(List<User> followingList) {
        this.followingList = followingList;
    }

    public List<User> getFollowingList() {
        return followingList;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
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
     *
     * @param follower the follower that wants to unfollow
     */
    public void unFollow(User follower) {
        this.followingList.remove(follower);
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
     * Add a new user review to the list of reviews this user has created
     *
     * @param review The review that a user made
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    /**
     * Determine if this User has the target review within their list of reviews
     *
     * @param id The unique id of the target review
     * @return True if the user has the target, false otherwise
     */
    public boolean hasReview(Long id) {
        return reviews.stream().anyMatch(review -> review.getId() == id);
    }

    /**
     * A method to return the Jaccard distance between this and another user across all of their reviews
     *
     * @param otherUser The other user that we wish to calculate the Jaccard distance between
     * @return The calculated Jaccard distance between the users, as an int
     */
    public double getJaccardDistanceReviews(User otherUser) {
        List<Review> followerFollowingList = otherUser.getReviews();

        // Jaccard distance between this user's review scores and another user's review scores using Guava
        Multiset<Double> localScores = HashMultiset.create();
        Multiset<Double> followerScores = HashMultiset.create();

        // Get a multiset for all the reviews written by the this user and then the other user
        localScores.addAll(getReviews().stream().mapToDouble(Review::getScore).boxed().collect(Collectors.toList()));
        followerScores.addAll(followerFollowingList.stream().mapToDouble(Review::getScore).boxed().collect(Collectors.toList()));

        // Jaccard distance is 0 if both sets are empty, which is an edge case.
        if (localScores.size() == 0 && followerScores.size() == 0) {
            return 0;
        }

        // Union in multisets is unstable, so we can expand out and do the long form calculation for the Jaccard Index
        // Long form is size of intersection divided by the size of set a + size of set b - size of intersection.
        double intersectSize = Multisets.intersection(localScores, followerScores).size();
        double jaccardIndex = intersectSize / (localScores.size() + followerScores.size() - intersectSize);

        // Jaccard distance is 1 - Jaccard Index
        return 1 - jaccardIndex;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", followingList=" + followingList +
//                ", reviews=" + reviews +
//                '}';
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
