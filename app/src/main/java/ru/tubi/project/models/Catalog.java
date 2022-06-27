package ru.tubi.project.models;

public class Catalog {
    private String imageURL;
    private String categoryName;

    public Catalog(String categoryName, String imageURL) {
        this.imageURL = imageURL;
        this.categoryName = categoryName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
