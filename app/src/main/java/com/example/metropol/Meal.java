package com.example.metropol;

public class Meal {
    private int id;
    private String name; //title
    private String pictureURL; //shortdesc
    private String description; //rating
    private int rating;//price
    private double price;//image

    public Meal(int id, String name, String pictureURL, String description, int rating, double price) {
        this.id = id;
        this.name = name;
        this.pictureURL = pictureURL;
        this.description = description;
        this.rating = rating;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getDescription() {return description;}

    public int getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }
}
