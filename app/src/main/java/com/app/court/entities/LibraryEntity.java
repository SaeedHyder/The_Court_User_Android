package com.app.court.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LibraryEntity {

    @SerializedName("libaray")
    @Expose
    private ArrayList<DocumentEntity> libaray;
    @SerializedName("Photos")
    @Expose
    private Integer photos;
    @SerializedName("videos")
    @Expose
    private Integer videos;
    @SerializedName("files")
    @Expose
    private Integer files;
    @SerializedName("cases")
    @Expose
    private ArrayList<CaseLibraryEntity> cases;

    public ArrayList<DocumentEntity> getLibaray() {
        return libaray;
    }

    public void setLibaray(ArrayList<DocumentEntity> libaray) {
        this.libaray = libaray;
    }

    public Integer getPhotos() {
        return photos;
    }

    public void setPhotos(Integer photos) {
        this.photos = photos;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }

    public Integer getFiles() {
        return files;
    }

    public void setFiles(Integer files) {
        this.files = files;
    }

    public ArrayList<CaseLibraryEntity> getCases() {
        return cases;
    }

    public void setCases(ArrayList<CaseLibraryEntity> cases) {
        this.cases = cases;
    }
}
