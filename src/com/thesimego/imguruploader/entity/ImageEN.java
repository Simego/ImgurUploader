package com.thesimego.imguruploader.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Simego
 */
public class ImageEN {

    private Long id;

    private String deletehash;

    private String link;

    private String description;

    private Date date;

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public enum Info {

        ID("id"),
        DELETEHASH("delete_hash"),
        LINK("link"),
        DESCRIPTION("description"),
        DATE("date");

        private String value;

        private Info(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
