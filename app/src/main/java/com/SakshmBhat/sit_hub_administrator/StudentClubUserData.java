package com.SakshmBhat.sit_hub_administrator;

public class StudentClubUserData {

    String AccessToken, name,clubName, imageUrl,userType,idProofUrl,clubLogoUrl,phoneNumber;

    public StudentClubUserData(String accessToken, String name, String clubName, String imageUrl, String userType, String idProofUrl,String clubLogoUrl,String phoneNumber) {
        AccessToken = accessToken;
        this.name = name;
        this.clubName = clubName;
        this.imageUrl = imageUrl;
        this.userType = userType;
        this.idProofUrl = idProofUrl;
        this.clubLogoUrl=clubLogoUrl;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClubLogoUrl() {
        return clubLogoUrl;
    }

    public void setClubLogoUrl(String clubLogoUrl) {
        this.clubLogoUrl = clubLogoUrl;
    }

    public StudentClubUserData() {
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(String idProofUrl) {
        this.idProofUrl = idProofUrl;
    }
}
