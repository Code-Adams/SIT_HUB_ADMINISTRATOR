package com.SakshmBhat.sit_hub_administrator;

public class SitOfficialAdminUserData {

    String accessToken, name, imageUrl,userType,idProofUrl,phoneNumber;

    public SitOfficialAdminUserData() {
    }


    public SitOfficialAdminUserData(String accessToken, String name, String imageUrl, String userType, String idProofUrl, String phoneNumber) {
        this.accessToken = accessToken;
        this.name = name;
        this.imageUrl = imageUrl;
        this.userType = userType;
        this.idProofUrl = idProofUrl;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
