package com.thesimego.imguruploader.entity.imgur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class AlbumData {

    private String id;

    private String title;

    private Object description;

    private Integer datetime;

    private String cover;

    private String account_url;

    private String privacy;

    private String layout;

    private Integer views;

    private String link;

    private String deletehash;

    private Integer images_count;

    //private List<ImgurImageData> images = new ArrayList<>();

    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public Integer getImages_count() {
        return images_count;
    }

    public void setImages_count(Integer images_count) {
        this.images_count = images_count;
    }

//    public List<ImgurImageData> getImages() {
//        return images;
//    }

//    public void setImages(List<ImgurImageData> images) {
//        this.images = images;
//    }

    public String getFullLink() {
        return "https://imgur.com/a/" + getId();
    }
    
    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (getTitle() != null) {
            stb.append("Title: ").append(getTitle()).append(", ");
        }
        stb.append("Link: ").append((getFullLink())).append(".");
        return stb.toString();
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
