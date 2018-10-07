package com.home.joseki.repositoryviewer.models;

public class License {

    private String spdx_id;

    private String license_name;

    private String license_url;

    private String key;

    private String license_node_id;

    public String getSpdx_id ()
    {
        return spdx_id;
    }

    public void setSpdx_id (String spdx_id)
    {
        this.spdx_id = spdx_id;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [spdx_id = "+spdx_id+", name = "+license_name+", url = "+license_url+", key = "+key+", node_id = "+license_node_id+"]";
    }

    public String getLicense_url() {
        return license_url;
    }

    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }

    public String getLicense_node_id() {
        return license_node_id;
    }

    public void setLicense_node_id(String license_node_id) {
        this.license_node_id = license_node_id;
    }

    public String getLicense_name() {
        return license_name;
    }

    public void setLicense_name(String license_name) {
        this.license_name = license_name;
    }
}
