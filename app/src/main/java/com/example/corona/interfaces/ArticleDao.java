package com.example.corona.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.corona.model.ModelRoom;

import java.util.List;

@Dao
public interface ArticleDao
{
    @Insert
    long addArticle(ModelRoom article);

    @Query("SELECT * FROM tbl_articles WHERE category LIKE :cat LIMIT :from_record_num,:records_per_page")
    List<ModelRoom> getSubjectArticles(int from_record_num, int records_per_page, String cat);

    @Query("SELECT * FROM tbl_articles WHERE year=:yearInput LIMIT :from_record_num,:records_per_page")
    List<ModelRoom> getYearsArticles(String yearInput,int from_record_num, int records_per_page);


//    @Query("SELECT * FROM tbl_articles WHERE title LIKE :str LIMIT :from_record_num,:records_per_page")
//    List<ModelRoom> searchOnTitle(String str, int from_record_num, int records_per_page);
//
//    @Query("SELECT * FROM tbl_articles WHERE abstractArticle LIKE :str LIMIT :from_record_num,:records_per_page")
//    List<ModelRoom> searchOnAbstract(String str, int from_record_num, int records_per_page);

    @Query("SELECT * FROM tbl_articles WHERE title LIKE :str")
    List<ModelRoom> searchOnTitle(String str);

    @Query("SELECT * FROM tbl_articles WHERE abstractArticle LIKE :str")
    List<ModelRoom> searchOnAbstract(String str);


    @Query("SELECT * FROM tbl_articles")
    List<ModelRoom> getAllArticles();
}
