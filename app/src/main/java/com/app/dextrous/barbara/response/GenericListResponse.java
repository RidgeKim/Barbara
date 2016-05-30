package com.app.dextrous.barbara.response;

import java.io.Serializable;
import java.util.List;

public class GenericListResponse<T> implements Serializable{
    private Boolean success;
    private String error;
    private List<T> items;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GenericListResponse{" +
                "success=" + success +
                ", items=" + items +
                ", error=" + error +
                '}';
    }
}
