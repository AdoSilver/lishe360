package com.cardeanutrition.lishe360app.Feedlist.model;

public class Feed {


    String name;
    String time;
    String description;
    String photoFeed;
    String url;
    String title;
  private String Maintitle;
  private String Profile;
    String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getMaintitle() {
        return Maintitle;
    }

    public void setMaintitle(String maintitle) {
        Maintitle = maintitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Feed() {
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getPhotoFeed() {
        return photoFeed;
    }

    public void setPhotoFeed(String photoFeed) {
        this.photoFeed = photoFeed;
    }
}
