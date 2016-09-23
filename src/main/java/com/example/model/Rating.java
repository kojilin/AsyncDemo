package com.example.model;

public class Rating {
    private final int score;

    public Rating(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Rating rating = (Rating) o;

        return score == rating.score;

    }

    @Override
    public int hashCode() {
        return score;
    }

    @Override
    public String toString() {
        return "Rating{" +
               "score=" + score +
               '}';
    }
}
