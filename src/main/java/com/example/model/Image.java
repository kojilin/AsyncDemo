package com.example.model;

public class Image {
    private final String path;

    public Image(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Image image = (Image) o;

        return path != null ? path.equals(image.path) : image.path == null;

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Image{" +
               "path='" + path + '\'' +
               '}';
    }
}
