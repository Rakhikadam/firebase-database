package com.Firebase_databaseEx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class hotellist {
    String name;
    String offer;
    String image;

    public hotellist(String name, String offer, String image) {
        this.name = name;
        this.offer = offer;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
