package com.home.joseki.repositoryviewer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

public class Weeks {

    private String w;

    private String d;

    private String c;

    private String a;

    public String getW ()
    {
        return w;
    }

    public void setW (String w)
    {
        this.w = w;
    }

    public String getD ()
    {
        return d;
    }

    public void setD (String d)
    {
        this.d = d;
    }

    public String getC ()
    {
        return c;
    }

    public void setC (String c)
    {
        this.c = c;
    }

    public String getA ()
    {
        return a;
    }

    public void setA (String a)
    {
        this.a = a;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [w = "+w+", d = "+d+", c = "+c+", a = "+a+"]";
    }
}
