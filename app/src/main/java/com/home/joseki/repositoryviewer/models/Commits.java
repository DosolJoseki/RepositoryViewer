package com.home.joseki.repositoryviewer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
public class Commits {

    public Commits(){
        node_id = "";
    }

    @ColumnInfo(name = "html_url_commits")
    private String html_url;

    @Embedded(prefix = "commits_")
    private Author author;

    @Ignore
    private List<Parents> parents;

    @Embedded
    private Commit commit;

    @ColumnInfo(name = "sha_commits")
    private String sha;

    private String comments_url;

    @Embedded
    private Committer committer;

    private String url;

    @PrimaryKey
    @ColumnInfo(name = "node_id_commits")
    @NotNull
    private String node_id;

    public String getHtml_url ()
    {
        return html_url;
    }

    public void setHtml_url (String html_url)
    {
        this.html_url = html_url;
    }

    public Author getAuthor ()
    {
        return author;
    }

    public void setAuthor (Author author)
    {
        this.author = author;
    }

    public List<Parents> getParents ()
    {
        return parents;
    }

    public void setParents (List<Parents> parents)
    {
        this.parents = parents;
    }

    public Commit getCommit ()
    {
        return commit;
    }

    public void setCommit (Commit commit)
    {
        this.commit = commit;
    }

    public String getSha ()
    {
        return sha;
    }

    public void setSha (String sha)
    {
        this.sha = sha;
    }

    public String getComments_url ()
    {
        return comments_url;
    }

    public void setComments_url (String comments_url)
    {
        this.comments_url = comments_url;
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

    @NonNull
    public String getNode_id ()
    {
        return node_id;
    }

    public void setNode_id (@NonNull String node_id)
    {
        this.node_id = node_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [html_url = "+html_url+", author = "+author+", parents = "+parents+", commit = "+commit+", sha = "+sha+", comments_url = "+comments_url+", committer = "+committer+", url = "+url+", node_id = "+node_id+"]";
    }
}