package com.app.dextrous.barbara.model;

import java.io.Serializable;
import java.util.Date;

public class InvestmentPlan implements Serializable {
    private String description;
    private String type;
    private int id;
    private float amount;
    private Date createdTS;
    private Date lastUpdatedTS;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(Date createdTS) {
        this.createdTS = createdTS;
    }

    public Date getLastUpdatedTS() {
        return lastUpdatedTS;
    }

    public void setLastUpdatedTS(Date lastUpdatedTS) {
        this.lastUpdatedTS = lastUpdatedTS;
    }

    @Override
    public String toString() {
        return "InvestmentPlan{" +
                "description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", amount=" + amount +
                ", createdTS=" + createdTS +
                ", lastUpdatedTS=" + lastUpdatedTS +
                '}';
    }
}
