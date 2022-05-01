package com.example.corona.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_articles")
public class ModelRoom
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String year;
    private String link;
    private String abstractArticle;
    private String category;
    private String pdfName;


    public ModelRoom(String year,String pdfName,String category,String title, String link, String abstractArticle ) {
        this.title = title;
        this.year = year;
        this.link = link;
        this.abstractArticle = abstractArticle;
        this.category = category;
        this.pdfName = pdfName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAbstractArticle() {
        return abstractArticle;
    }

    public void setAbstractArticle(String abstractArticle) {
        this.abstractArticle = abstractArticle;
    }
}
