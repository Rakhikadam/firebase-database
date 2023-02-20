package com.Firebase_databaseEx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class hotellist {
    String name;
    String offer;
    String image;
    String hotel_id;

    public hotellist(String name, String offer, String image, String hotel_id) {
        this.name = name;
        this.offer = offer;
        this.image = image;
        this.hotel_id = hotel_id;
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

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }
}
