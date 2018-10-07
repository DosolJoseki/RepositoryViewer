package com.home.joseki.repositoryviewer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

@Entity
public class Commit {

    private String comment_count;

    private String message;

    @Embedded(prefix = "commit_")
    private Author author;

    @Embedded
    private Tree tree;

    @Embedded
    private Verification verification;

    @Embedded(prefix = "commit_")
    private Committer committer;

    @ColumnInfo(name = "url_commit")
    private String url;

    public String getComment_count ()
    {
        return comment_count;
    }

    public void setComment_count (String comment_count)
    {
        this.comment_count = comment_count;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Author getAuthor ()
    {
        return author;
    }

    public void setAuthor (Author author)
    {
        this.author = author;
    }

    public Tree getTree ()
    {
        return tree;
    }

    public void setTree (Tree tree)
    {
        this.tree = tree;
    }

    public Verification getVerification ()
    {
        return verification;
    }

    public void setVerification (Verification verification)
    {
        this.verification = verification;
    }

    public Committer getCommitter ()
    {
        return committer;
    }

    public void setCommitter (Committer committer)
    {
        this.committer = committer;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [comment_count = "+comment_count+", message = "+message+", author = "+author+", tree = "+tree+", verification = "+verification+", committer = "+committer+", url = "+url+"]";
    }
}
