package com.home.joseki.repositoryviewer.models;

import android.arch.persistence.room.Entity;

@Entity
public class Verification {

    private String reason;

    private String verified;

    public String getReason ()
    {
        return reason;
    }

    public void setReason (String reason)
    {
        this.reason = reason;
    }

    public String getVerified ()
    {
        return verified;
    }

    public void setVerified (String verified)
    {
        this.verified = verified;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reason = "+reason+", verified = "+verified+"]";
    }
}
