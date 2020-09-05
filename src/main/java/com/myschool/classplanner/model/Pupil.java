package com.myschool.classplanner.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Pupil {

    private Integer id;
    private String name;
    private Gender gender;
    private Pupil friend;
    private Pupil secondaryFriend;
    private Pupil tertiaryFriend;
    private Boolean specialNeeds;
    private SchoolClass schoolClass;
    private SchoolClass fixedSchoolClass;

    public Pupil() {}

    public Pupil(Integer id, String name, Gender gender, Boolean specialNeeds) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.specialNeeds = specialNeeds;
    }

    public Pupil(Integer id, String name, Gender gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.specialNeeds = false;
    }

    public Integer getId() {
        return id;
    }

    public void setFriend(Pupil friend) {
        this.friend = friend;
    }

    public void setSecondaryFriend(Pupil friend) {
        this.secondaryFriend = friend;
    }

    public void setTertiaryFriend(Pupil friend) {
        this.tertiaryFriend = friend;
    }

    public Pupil getFriend() {
        return friend;
    }

    public Pupil getSecondaryFriend() {
        return secondaryFriend;
    }

    public Pupil getTertiaryFriend() {
        return tertiaryFriend;
    }

    public String getName() { return name; }

    public Gender getGender() {
        return gender;
    }

    public Boolean getSpecialNeeds() { return specialNeeds; }

    @PlanningVariable (valueRangeProviderRefs = {"schoolClassRange"})
    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public SchoolClass getFixedSchoolClass() {
        return fixedSchoolClass;
    }

    public void setFixedSchoolClass(SchoolClass fixedSchoolClass) {
        this.fixedSchoolClass = fixedSchoolClass;
    }

    public void setSpecialNeeds(Boolean specialNeeds) { this.specialNeeds = specialNeeds; }

    public String toString() {
        return String.format("\r\n%s (%s) is assigned to %s. Class restriction: %b. Friend restriction: %b",
            name, gender.toString(), schoolClass,
            fixedSchoolClass == null || fixedSchoolClass == schoolClass,
            friend == null || friend.getSchoolClass() == schoolClass);
    }
}
