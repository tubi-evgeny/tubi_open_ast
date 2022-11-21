package ru.tubi.project.models;

public class Catalog {
    private String imageURL;
    private int catalog_id;
    private String categoryName;

    public Catalog(String categoryName, String imageURL) {
        this.imageURL = imageURL;
        this.categoryName = categoryName;
    }
    //ActivityCatalog / splitResult();
    public Catalog(String categoryName, String imageURL, int catalog_id) {
        this.imageURL = imageURL;
        this.categoryName = categoryName;
        this.catalog_id=catalog_id;
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

    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }
}
