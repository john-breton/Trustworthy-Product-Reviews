package com.productreviews.models;

import com.productreviews.models.common.Category;
import org.hibernate.annotations.GeneratorType;

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
     * A unique ID for the Product object for persistence purposes
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    /**
     * The name of the product. This can likely support HTML encodings
     */
    @Column(unique = true)
    private String name;

    /**
     * The textual description of a product. This can likely support HTML encodings
     */
    private String description;

    /**
     * A reference to an image that is associated with this product
     */
    private String image;

    /**
     * A list of the reviews on the product
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "associatedProduct")
    private List<Review> reviews;

    /**
     * The category this product belongs to
     */
    private Category category;

    /**
     * Create a new empty product
     */
    public Product() {
        reviews = new ArrayList<>();
    }

    /**
     * Create a new product with a name and an image
     *
     * @param name  The name of the product
     * @param image The image that will be used for the product, placed in the /images/ directory
     */
    public Product(String name, String image) {
        this.name = name;
        this.image = image;
        reviews = new ArrayList<>();
    }

    /**
     * Create an expanded new product with a name, image, and specific id
     *
     * @param name  The name of the product
     * @param image The image that will be used for the product, placed in the /images/ directory
     * @param id    The id number to be associated with this product.
     */
    public Product(String name, String image, Long id) {
        this.name = name;
        this.id = id;
        this.image = image;
        reviews = new ArrayList<>();
    }

    /**
     * Create an expanded new product with a name, image, id, category, and description.
     *
     * @param name        The name of the product
     * @param image       The image that will be used for the product, placed in the /images/ directory
     * @param id          The id number to be associated with this product.
     * @param description The description for this product, supports HTML encoding
     * @param category    The category for this product, from the Category enumeration.
     */
    public Product(String name, String image, Long id, String description, Category category) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.description = description;
        this.category = category;
        reviews = new ArrayList<>();
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

    public String getImage() {
        return image;
    }

    public void setImage(String hrefImage) {
        this.image = hrefImage;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Add a review to the list of reviews associated with this product
     *
     * @param review The review for the product
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    /**
     * Computes the average rating of the product
     *
     * @return The average rating of the product, as a double
     */
    public String getAverageRating() {
        if (reviews.size() == 0) {
            return "0";
        } else {
            double sum = reviews.stream().mapToDouble(Review::getScore).sum();
            return String.format("%.2f", sum / reviews.size());
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", reviews=" + reviews +
                ", category=" + category +
                '}';
    }
}
