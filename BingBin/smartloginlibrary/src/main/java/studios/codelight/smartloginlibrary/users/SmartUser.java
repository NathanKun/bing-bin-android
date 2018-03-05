package studios.codelight.smartloginlibrary.users;

import java.io.Serializable;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/23/2015.
 */
public class SmartUser implements Serializable{

    private String userId;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String birthday;
    private int gender;
    private String profileLink;

    private String avatarUrl;
    private String token;
    private String pseudo;
    private int ecoPoint;
    private int sunPoint;
    private int rabbit;
    private int leaf;


    public SmartUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getAvatarUrl() { return avatarUrl; }

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getEcoPoint() {
        return ecoPoint;
    }

    public void setEcoPoint(int ecoPoint) {
        this.ecoPoint = ecoPoint;
    }

    public int getSunPoint() { return sunPoint; }

    public void setSunPoint(int sunPoint) {
        this.sunPoint = sunPoint;
    }

    public int getRabbit() {
        return rabbit;
    }

    public void setRabbit(int rabbit) {
        this.rabbit = rabbit;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }
    @Override
    public String toString() {
        return "SmartUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender=" + gender +
                ", profileLink='" + profileLink + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", token='" + token + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", ecoPoint='" + ecoPoint + '\'' +
                ", rabbit='" + rabbit + '\'' +
                ", leaf='" + leaf + '\'' +
                '}';
    }
}
