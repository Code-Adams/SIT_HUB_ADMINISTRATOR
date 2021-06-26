package com.SakshmBhat.sit_hub_administrator;

public class ClubData {

    private String clubName,clubDescription,clubLogoUrl,validClub,phoneNumber,clubType,clubWebPageUrl;

    public ClubData() {
    }

    public ClubData(String clubName, String clubDescription, String clubLogoUrl, String validClub, String phoneNumber, String clubType, String clubWebPageUrl) {
        this.clubName = clubName;
        this.clubDescription = clubDescription;
        this.clubLogoUrl = clubLogoUrl;
        this.validClub = validClub;
        this.phoneNumber = phoneNumber;
        this.clubType = clubType;
        this.clubWebPageUrl = clubWebPageUrl;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getValidClub() {
        return validClub;
    }

    public void setValidClub(String validClub) {
        this.validClub = validClub;
    }

    public String getClubLogoUrl() {
        return clubLogoUrl;
    }

    public void setClubLogoUrl(String clubLogoUrl) {
        this.clubLogoUrl = clubLogoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    public String getClubWebPageUrl() {
        return clubWebPageUrl;
    }

    public void setClubWebPageUrl(String clubWebPageUrl) {
        this.clubWebPageUrl = clubWebPageUrl;
    }
}
