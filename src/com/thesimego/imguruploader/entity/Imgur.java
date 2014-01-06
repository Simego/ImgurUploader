package com.thesimego.imguruploader.entity;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("com.googlecode.jsonschema2pojo")
public class Imgur {

    private ImgurData data;
    private Boolean success;
    private Integer status;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public ImgurData getData() {
        return data;
    }

    public void setData(ImgurData data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
