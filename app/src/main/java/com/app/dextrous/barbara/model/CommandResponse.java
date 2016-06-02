package com.app.dextrous.barbara.model;


import java.io.Serializable;
import java.util.Date;

public class CommandResponse implements Serializable {
    private Date associatedTime;
    private Boolean hasGreetingText;
    private Boolean isBudgetChange;
    private Boolean isBudgetCheck;
    private Boolean isCreditAccount;
    private Boolean isCurrentBalanceRequest;
    private Boolean isPromotionsCheck;
    private Boolean isReadRequest;
    private Boolean isReadSentTransaction;
    private Boolean isReminderRequest;
    private Boolean isScheduleRequest;
    private Boolean isTransactionRequest;
    private String referredAmount;
    private String referredUser;
    private Boolean requireAuthentication;
    private String responseText;
    private String scheduledResponseText;


    public Date getAssociatedTime() {
        return associatedTime;
    }

    public void setAssociatedTime(Date associatedTime) {
        this.associatedTime = associatedTime;
    }

    public Boolean getHasGreetingText() {
        return hasGreetingText;
    }

    public void setHasGreetingText(Boolean hasGreetingText) {
        this.hasGreetingText = hasGreetingText;
    }

    public Boolean getIsBudgetChange() {
        return isBudgetChange;
    }

    public void setIsBudgetChange(Boolean isBudgetChange) {
        this.isBudgetChange = isBudgetChange;
    }

    public Boolean getIsBudgetCheck() {
        return isBudgetCheck;
    }

    public void setIsBudgetCheck(Boolean isBudgetCheck) {
        this.isBudgetCheck = isBudgetCheck;
    }

    public Boolean getIsCreditAccount() {
        return isCreditAccount;
    }

    public void setIsCreditAccount(Boolean isCreditAccount) {
        this.isCreditAccount = isCreditAccount;
    }

    public Boolean getIsCurrentBalanceRequest() {
        return isCurrentBalanceRequest;
    }

    public void setIsCurrentBalanceRequest(Boolean isCurrentBalanceRequest) {
        this.isCurrentBalanceRequest = isCurrentBalanceRequest;
    }

    public Boolean getIsPromotionsCheck() {
        return isPromotionsCheck;
    }

    public void setIsPromotionsCheck(Boolean isPromotionsCheck) {
        this.isPromotionsCheck = isPromotionsCheck;
    }

    public Boolean getIsReadRequest() {
        return isReadRequest;
    }

    public void setIsReadRequest(Boolean isReadRequest) {
        this.isReadRequest = isReadRequest;
    }

    public Boolean getIsReadSentTransaction() {
        return isReadSentTransaction;
    }

    public void setIsReadSentTransaction(Boolean isReadSentTransaction) {
        this.isReadSentTransaction = isReadSentTransaction;
    }

    public Boolean getIsReminderRequest() {
        return isReminderRequest;
    }

    public void setIsReminderRequest(Boolean isReminderRequest) {
        this.isReminderRequest = isReminderRequest;
    }

    public Boolean getIsScheduleRequest() {
        return isScheduleRequest;
    }

    public void setIsScheduleRequest(Boolean isScheduleRequest) {
        this.isScheduleRequest = isScheduleRequest;
    }

    public Boolean getIsTransactionRequest() {
        return isTransactionRequest;
    }

    public void setIsTransactionRequest(Boolean isTransactionRequest) {
        this.isTransactionRequest = isTransactionRequest;
    }

    public String getReferredAmount() {
        return referredAmount;
    }

    public void setReferredAmount(String referredAmount) {
        this.referredAmount = referredAmount;
    }

    public String getReferredUser() {
        return referredUser;
    }

    public void setReferredUser(String referredUser) {
        this.referredUser = referredUser;
    }

    public Boolean getRequireAuthentication() {
        return requireAuthentication;
    }

    public void setRequireAuthentication(Boolean requireAuthentication) {
        this.requireAuthentication = requireAuthentication;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getScheduledResponseText() {
        return scheduledResponseText;
    }

    public void setScheduledResponseText(String scheduledResponseText) {
        this.scheduledResponseText = scheduledResponseText;
    }

    @Override
    public String toString() {
        return "CommandResponse{" +
                "associatedTime=" + associatedTime +
                ", hasGreetingText=" + hasGreetingText +
                ", isBudgetChange=" + isBudgetChange +
                ", isBudgetCheck=" + isBudgetCheck +
                ", isCreditAccount=" + isCreditAccount +
                ", isCurrentBalanceRequest=" + isCurrentBalanceRequest +
                ", isPromotionsCheck=" + isPromotionsCheck +
                ", isReadRequest=" + isReadRequest +
                ", isReadSentTransaction=" + isReadSentTransaction +
                ", isReminderRequest=" + isReminderRequest +
                ", isScheduleRequest=" + isScheduleRequest +
                ", isTransactionRequest=" + isTransactionRequest +
                ", referredAmount=" + referredAmount +
                ", referredUser='" + referredUser + '\'' +
                ", requireAuthentication=" + requireAuthentication +
                ", responseText='" + responseText + '\'' +
                ", scheduledResponseText='" + scheduledResponseText + '\'' +
                '}';
    }
}
