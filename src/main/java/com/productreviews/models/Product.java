package com.productreviews.models;

import com.productreviews.models.common.Category;
import jdk.tools.jlink.internal.plugins.ExcludePlugin;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Product represents a minimal entity that has a name,
 * textual description, and an image reference. The image
 * itself is not persisted, but the href link is maintained.
 */
@Entity
public class Product {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * A unique ID for the Product object for persistence purposes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
     * The average rating of a product
     */
    private double averageRating;

    /**
     * Create a new product with a name and an image
     *
     * @param name  The name of the product
     * @param image The image that will be used for the product, placed in the /images/ directory
     */
    public Product(String name, String image) {
        this.name = name;
        this.image = image;
        averageRating=0;
        reviews = new ArrayList<>();
    }

    /**
     * Create an expanded new product with a name, image, and specific id
     *
     * @param name  The name of the product
     * @param image The image that will be used for the product, placed in the /images/ directory
     * @param category  the category that the product belongs to
     * @param id    The id number to be associated with this product.
     */
    public Product(String name, String image,Category category,Long id) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.category=category;
        averageRating=0;
        reviews = new ArrayList<>();
    }

    /**
     * Create an expanded new product with a name, image, and specific id
     *
     * @param name  The name of the product
     * @param image The image that will be used for the product, placed in the /images/ directory
     * @param id    The id number to be associated with this product.
     */
    public Product(String name, String image,Long id) {
        this.name = name;
        this.id = id;
        this.image = image;
        averageRating=0;
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

    /**
     * Create an expanded new product with a name, image, id, category, and description.
     *
     * @param name        The name of the product
     * @param image       The image that will be used for the product, placed in the /images/ directory
     * @param description The description for this product, supports HTML encoding
     * @param category    The category for this product, from the Category enumeration.
     */
    public Product(String name, String image, String description, Category category) {
        this.name = name;
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
        updateAverageRating();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAverageRating() {
        return Double.parseDouble(df.format(averageRating));
    }

    public void setAverageRating(double averageRating){
        this.averageRating=averageRating;
    }

    public void updateAverageRating(){
        averageRating = reviews.stream().mapToDouble(Review::getScore).sum()/reviews.size();
    }

    /**
     * Add a review to the list of reviews associated with this product
     *
     * @param review The review for the product
     */
    public void addReview(Review review) {
        reviews.add(review);
        updateAverageRating();
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

    /**
     * Given a string, creates a new product
     * @param product_str a comma separated product in the format name, image, category, description, url
     * @return
     */
    public static Product createProductFromString(String product_str){

        String[] params = product_str.split(",");

        int i = 0;
        String name = params[i].trim();
        String image = "products/" + params[++i].trim();
        String category = params[++i].trim();
        String description = params[++i].trim();
//        String url = params[++i]; // not used atm

        Product product;
        try {
            product = new Product(name, image, description, Category.valueOf(category.toUpperCase()));
        } catch (Exception e){
            System.out.println("Exception creating product " + e);
            return null;
        }

        return product;
    }

}
