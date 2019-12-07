package com.example.ideaskill;

public class ModelPost {
    String pId,uAim,uTimeOfPost,uEmail,uType,uId,uName,ProfileImage,Progress;

    public ModelPost(){

    }

    public ModelPost(String pId, String uAim, String uTimeOfPost, String uEmail, String uType,String uId,String uName,String ProfileImage,String Progress) {
        this.pId = pId;
        this.uAim = uAim;
        this.uTimeOfPost = uTimeOfPost;
        this.uEmail = uEmail;
        this.uType = uType;
        this.uId = uId;
        this.uName = uName;
        this.ProfileImage = ProfileImage;
        this.Progress=Progress;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String uProfileImage) {
        this.ProfileImage = uProfileImage;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String uProgress) {
        this.Progress = uProgress;
    }

    public String getuId() {
        return uId;
    }

    public String getuName() {
        return uName;
    }



    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuAim() {
        return uAim;
    }

    public void setuAim(String uAim) {
        this.uAim = uAim;
    }

    public String getuTimeOfPost() {
        return uTimeOfPost;
    }

    public void setuTimeOfPost(String uTimeOfPost) {
        this.uTimeOfPost = uTimeOfPost;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }
}
