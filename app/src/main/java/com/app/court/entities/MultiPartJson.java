package com.app.court.entities;

import java.io.File;

public class MultiPartJson {

    private File thumb_nail;
    private String type;
    private File file;

    public MultiPartJson(File thumb_nail, String type, File file) {
        this.thumb_nail = thumb_nail;
        this.type = type;
        this.file = file;
    }

    public File getThumb_nail() {
        return thumb_nail;
    }

    public void setThumb_nail(File thumb_nail) {
        this.thumb_nail = thumb_nail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
