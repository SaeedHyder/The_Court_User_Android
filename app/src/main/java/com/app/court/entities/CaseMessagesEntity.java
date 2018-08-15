package com.app.court.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CaseMessagesEntity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thread_id")
    @Expose
    private String threadId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("sender_detail")
    @Expose
    private SenderRecieverDetail senderDetail;
    @SerializedName("receiver_detail")
    @Expose
    private SenderRecieverDetail receiverDetail;
    @SerializedName("document_detail")
    @Expose
    private List<DocumentEntity> documentDetail = null;

    public List<DocumentEntity> getDocumentDetail() {
        return documentDetail;
    }

    public void setDocumentDetail(List<DocumentEntity> documentDetail) {
        this.documentDetail = documentDetail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public SenderRecieverDetail getSenderDetail() {
        return senderDetail;
    }

    public void setSenderDetail(SenderRecieverDetail senderDetail) {
        this.senderDetail = senderDetail;
    }

    public SenderRecieverDetail getReceiverDetail() {
        return receiverDetail;
    }

    public void setReceiverDetail(SenderRecieverDetail receiverDetail) {
        this.receiverDetail = receiverDetail;
    }

}
