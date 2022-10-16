package model;

public class Ingredient {

    String hash;

    public Ingredient(String hash) {
        this.hash = hash;
    }

    public Ingredient() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
