package com.inhatc.primrose;

public class Flower {
    private String fname;
    private String floriography;
    private String image;

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getFloriography() {
        return floriography;
    }
    public void setFloriography(String floriography) {
        this.floriography = floriography;
    }
    public Flower() {
    }
    public Flower(String fname, String floriography, String image) {
        this.fname = fname;
        this.floriography = floriography;
        this.image = image;
    }
}