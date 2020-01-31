package com.irvin.makeapp.Models;

public class CustomerModel {

    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String birthday;
    private String age;
    private String occupation;
    private String email;
    private String contactNumber;
    private String bestTimeToBeContacted;
    private String referredBy;
    private String skinType;
    private String skinConcern;
    private String skinTone;
    private String interests;
    private String photoUrl;

    public CustomerModel(String firstName, String lastName, String middleName, String address,
                         String birthday, String age, String occupation, String email, String contactNumber,
                         String bestTimeToBeContacted, String referredBy,
                         String skinType, String skinConcern, String skinTone, String interests, String photoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
        this.birthday = birthday;
        this.age = age;
        this.occupation = occupation;
        this.email = email;
        this.contactNumber = contactNumber;
        this.bestTimeToBeContacted = bestTimeToBeContacted;
        this.referredBy = referredBy;
        this.skinType = skinType;
        this.skinConcern = skinConcern;
        this.skinTone = skinTone;
        this.interests = interests;
        this.photoUrl = photoUrl;
    }

    public CustomerModel(){}

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getBestTimeToBeContacted() {
        return bestTimeToBeContacted;
    }

    public void setBestTimeToBeContacted(String bestTimeToBeContacted) {
        this.bestTimeToBeContacted = bestTimeToBeContacted;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getSkinConcern() {
        return skinConcern;
    }

    public void setSkinConcern(String skinConcern) {
        this.skinConcern = skinConcern;
    }

    public String getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(String skinTone) {
        this.skinTone = skinTone;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName(){
        return  this.firstName + " " + this.lastName;
    }
}
