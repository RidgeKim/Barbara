package com.app.dextrous.barbara.model;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Date createdTS;
    private String currencyType;
    private String firstName;
    private String fullName;
    private Integer id;
    private String lastName;
    private Date lastUpdatedTS;
    private String username;
    private Float wallet;
    private Float credit;
    private String speakerProfileId;
    private UserPreference preferences;

    public Date getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(Date createdTS) {
        this.createdTS = createdTS;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastUpdatedTS() {
        return lastUpdatedTS;
    }

    public void setLastUpdatedTS(Date lastUpdatedTS) {
        this.lastUpdatedTS = lastUpdatedTS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getWallet() {
        return wallet;
    }

    public void setWallet(Float wallet) {
        this.wallet = wallet;
    }

    public Float getCredit() {
        return credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public String getSpeakerProfileId() {
        return speakerProfileId;
    }

    public void setSpeakerProfileId(String speakerProfileId) {
        this.speakerProfileId = speakerProfileId;
    }

    public UserPreference getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreference preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "User{" +
                "createdTS=" + createdTS +
                ", currencyType='" + currencyType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", lastUpdatedTS=" + lastUpdatedTS +
                ", username='" + username + '\'' +
                ", wallet=" + wallet +
                ", credit=" + credit +
                ", speakerProfileId=" + speakerProfileId +
                ", preferences=" + preferences +
                '}';
    }
}
