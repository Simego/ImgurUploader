package com.thesimego.imguruploader.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class ImgurData {

    private String id;
    private Object title;
    private Object description;
    private Integer datetime;
    private String type;
    private Boolean animated;
    private Integer width;
    private Integer height;
    private Integer size;
    private Integer views;
    private Integer bandwidth;
    private Boolean favorite;
    private Object nsfw;
    private Object section;
    private String deletehash;
    private String link;
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAnimated() {
        return animated;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Object getNsfw() {
        return nsfw;
    }

    public void setNsfw(Object nsfw) {
        this.nsfw = nsfw;
    }

    public Object getSection() {
        return section;
    }

    public void setSection(Object section) {
        this.section = section;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (getTitle() != null) {
            stb.append("Title: ").append(getTitle()).append(", ");
        }
        if (getDescription() != null) {
            stb.append("Description: ").append(getDescription()).append(", ");
        }
        stb.append("Size: ").append((getSize() / 1024)).append("kb, ");
        stb.append("Width: ").append(getWidth()).append(", ");
        stb.append("Height: ").append(getHeight()).append(", ");

        stb.append("Uploaded at: ").append(getDateString()).append(".");
        return stb.toString();
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        long epochTime = ((long) getDatetime()) * 1000;
        date.setTime(epochTime);
        return sdf.format(date);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
