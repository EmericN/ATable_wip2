package com.emeric.nicot.atable.models;


public class FirebaseSalonAdmin {

    private String salon;
    private String lastMessage;
    private String id;

    public FirebaseSalonAdmin() {
    }

    public FirebaseSalonAdmin(String salon, String id, String lastMessage) {
        this.salon = salon;
        this.id = id;
        this.lastMessage = lastMessage;
    }

    public String getSalon() {
        return salon;
    }

    public String getSalonId() {
        return id;
    }

    public String getSalonLastMessage() {
        return lastMessage;
    }

    public int size() {
        return salon.length();
    }


}
