package com.app.dextrous.barbara.model;


import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private int id;
    private int fromUserId;
    private int toUserId;
    private User fromUser;
    private User toUser;
    private float amount;
    private String description;
    private Date createdTS;
    private Date lastUpdatedTS;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Transaction{" +
                "id=" + id +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdTS=" + createdTS +
                ", lastUpdatedTS=" + lastUpdatedTS +
                '}';
    }
}
