package com.SakshmBhat.sit_hub_administrator;

public class ClubData {

    private String clubName,clubDescription,clubLogoUrl,validClub;

    public ClubData() {
    }

    public ClubData(String clubName, String clubDescription, String clubLogoUrl, String validClub) {
        this.clubName = clubName;
        this.clubDescription = clubDescription;
        this.clubLogoUrl = clubLogoUrl;
        this.validClub = validClub;
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
}
