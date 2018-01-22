package com.example.test.testproj.models;

/**
 *Model("КОСТЫЛЬ") that helps to deserialize data from {@link com.google.gson.Gson}
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */
public class JsonShow {
    private JsonShowData show;

    public JsonShow(){

    }
    public JsonShow(JsonShowData show) {
        this.show = show;
    }

    public JsonShowData getShow() {
        return show;
    }

    public void setShow(JsonShowData show) {
        this.show = show;
    }
    public static class JsonShowData {
        private String url;
        private String name;
        private Rating rating;
        private Image image;
        private String summary;

        public JsonShowData() {

        }

        public JsonShowData(String url, String name, Rating rating, Image image, String summary) {
            this.url = url;
            this.name = name;
            this.rating = rating;
            this.image = image;
            this.summary = summary;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Rating getRating() {
            return rating;
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
    public static class Image {
        private String medium;
        private String original;

        public Image() {

        }

        public Image(String medium, String original) {
            this.medium = medium;
            this.original = original;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }
    public static class Rating {
        private double average;
        public Rating(){

        }
        public Rating(Double average) {
            this.average = average;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }
    }
}
