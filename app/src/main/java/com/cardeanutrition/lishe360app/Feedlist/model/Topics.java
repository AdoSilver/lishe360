package com.cardeanutrition.lishe360app.Feedlist.model;

/**
 * Created by JULIUS JOHN on 4/5/2018.
 */

public class Topics {
    String maintitle;
    String descption;
    String subtitle;
    String descption_one;
    String photo;
    String time;


    public Topics(){

    }

    public Topics(String maintitle,String descption,String subtitle,String descption_one,String photo,String time){
        this.maintitle = maintitle;
        this.descption = descption;
        this.subtitle = subtitle;
        this.descption_one = descption_one;
        this.photo = photo;
        this.time = time;
    }

    public String getMaintitle() {
        return maintitle;
    }

    public void setMaintitle(String maintitle) {
        this.maintitle = maintitle;
    }

    public String getDescption() {
        return descption;
    }

    public void setDescption(String descption) {
        this.descption = descption;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescption_one() {
        return descption_one;
    }

    public void setDescption_one(String descption_one) {
        this.descption_one = descption_one;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
