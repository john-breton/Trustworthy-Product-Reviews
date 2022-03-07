package com.productreviews.models;

import javax.persistence.*;
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
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * A unique username used to represent the user's identity.
     */
    private String username;

    /**
     * A list contained the users a particular user is following.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity=User.class)
    private List<User> followingList;

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

    public void addFollowing(User following) {
        // We might want to consider a hash map to have Jaccard distance info
        // ready for lookup. Extra argument would be the distance.
        followingList.add(following);
    }

    public List<User> getFollowingList() {
        return followingList;
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
