package com.app.court.entities;

import java.io.File;

public class DocumentArray {

    File file;
    String type;
    File thumb_nail;

    public DocumentArray(File file, String type, File thumb_nail) {
        this.file = file;
        this.type = type;
        this.thumb_nail = thumb_nail;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getThumb_nail() {
        return thumb_nail;
    }

    public void setThumb_nail(File thumb_nail) {
        this.thumb_nail = thumb_nail;
    }
}
