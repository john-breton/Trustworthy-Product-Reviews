package com.productreviews.Models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Product {

    //id of the product
    @Id
    private Long id;

    //name of the product
    private String name;

    //image of the product
    private String image;

    //reviews of the product
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
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
        this.image=image;
        reviews = new ArrayList<Review>();
    }

    public Product(String name, String image, Long id){
        this.name=name;
        this.id=id;
        this.image=image;
        reviews = new ArrayList<Review>();
    }

    /**
     * getter for the name of the product
     * @return name of the product
     */
    public String getName(){
        return name;
    }

    /**
     * getter for the reviews of the product
     * @return reviews of the product
     */
    public List<Review> getReviews(){
        return reviews;
    }

    /**
     * add a product review
     * @param review review for the product
     */
    public void addReview(Review review){
        reviews.add(review);
    }

    public int getAverageRating(){
        if(reviews.size() == 0){
            return 0;
        }else {
            int sum=0;
            for(int i=0; i<reviews.size();i++ ){
                sum+=reviews.get(i).getRating();
            }
            return sum/reviews.size();
        }
    }

    public Long getId(){
        return id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setReviews(ArrayList<Review> reviews)
    {
        this.reviews=reviews;
    }

    public void setId(Long id){
        this.id=id;
    }

    public void setImage(String image){
        this.image=image;
    }

    public String getImage(){
        return image;
    }
}
