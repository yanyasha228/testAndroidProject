package com.example.test.testproj.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Simple model of the Show
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */
public class Show {
    private long id;
    private String name;
    private String description;
    private String image;
    private String url;
    private double rate;
    private int fav;

    public Show() {

    }


    public Show(long id, String name, String description, String image, String url, double rate, int fav) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.url = url;
        this.rate = rate;
        this.fav = fav;
    }

    public Show(String name, String description, String image, String url, double rate, int fav) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.url = url;
        this.rate = rate;
        this.fav = fav;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }
}
