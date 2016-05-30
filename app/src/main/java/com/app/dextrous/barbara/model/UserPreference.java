package com.app.dextrous.barbara.model;


import java.io.Serializable;
import java.util.Date;

public class UserPreference implements Serializable {
    private Integer id;
    private float budget;
    private String securityQuestion;
    private String nickName;
    private Date createdTS;
    private Date lastUpdatedTS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
        return "UserPreference{" +
                "id=" + id +
                ", budget=" + budget +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createdTS=" + createdTS +
                ", lastUpdatedTS=" + lastUpdatedTS +
                '}';
    }
}
