package com.productreviews.models;

import java.util.ArrayList;
import java.util.List;
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
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
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

    /**
     * A list of the reviews on the product
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "associatedProduct")
    private List<Review> reviews;

    /**
     * create a new product
     */
    public Product(){
        reviews = new ArrayList<Review>();
    }

    /**
     * create a new product with a name and an image
     * @param name
     * @param image
     */
    public Product(String name, String image){
        this.name=name;
        this.hrefImage =image;
        reviews = new ArrayList<Review>();
    }

    public Product(String name, String image, Long id){
        this.name=name;
        this.id=id;
        hrefImage =image;
        reviews = new ArrayList<Review>();
    }

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

    /**
     * getter for the reviews of the product
     * @return reviews of the product
     */
    public List<Review> getReviews(){
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews)
    {
        this.reviews=reviews;
    }

    /**
     * add a product review
     * @param review review for the product
     */
    public void addReview(Review review){
        reviews.add(review);
    }

    /**
     * Computes the average rating of the product
     * @return average rating of the product
     */
    public String getAverageRating() {
        if (reviews.size() == 0) {
            return "0";
        } else {
            float sum = 0;
            for (int i = 0; i < reviews.size(); i++) {
                sum += reviews.get(i).getScore();
            }
            return String.format("%.2f",sum/reviews.size());
        }
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
