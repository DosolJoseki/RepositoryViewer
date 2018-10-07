package com.home.joseki.repositoryviewer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.home.joseki.repositoryviewer.dal.ConvertorsWeeks;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(primaryKeys = {"contr_id", "url"})
public class Contributors {

    @NotNull
    private String url;

    @NotNull
    private String contr_id;

    private String total;

    @Embedded(prefix = "contributors_")
    private Author author;

    @ColumnInfo(name = "contributors_week")
    @TypeConverters({ConvertorsWeeks.class})
    private List<Weeks> weeks;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public Author getAuthor ()
    {
        return author;
    }

    public void setAuthor (Author author)
    {
        this.author = author;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", author = "+author+", weeks = "+weeks+"]";
    }

    public List<Weeks> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Weeks> weeks) {
        this.weeks = weeks;
    }

    public String getContr_id() {
        return author.getId();
    }

    public void setContr_id(String contr_id) {
        this.contr_id = contr_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}