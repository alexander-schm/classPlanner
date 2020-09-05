package com.myschool.classplanner.service;

import com.myschool.classplanner.model.Gender;
import com.myschool.classplanner.model.Pupil;
import com.myschool.classplanner.model.School;
import com.myschool.classplanner.model.SchoolClass;

import java.util.ArrayList;
import java.util.List;

public class ExampleFactory {
    public static School createSimpleExample() {
        School unsolvedSchool = new School();
        unsolvedSchool.getSchoolClasses().add(new SchoolClass(1, "Mausklasse"));
        unsolvedSchool.getSchoolClasses().add(new SchoolClass(2, "Giraffenklasse"));
        unsolvedSchool.setPupilList(generatePupils());
        unsolvedSchool.getPupilList().get(0).setFriend(unsolvedSchool.getPupilList().get(1));
        unsolvedSchool.getPupilList().get(1).setFriend(unsolvedSchool.getPupilList().get(2));
        unsolvedSchool.getPupilList().get(2).setFriend(unsolvedSchool.getPupilList().get(3));
        unsolvedSchool.getPupilList().get(3).setFriend(unsolvedSchool.getPupilList().get(0));
        unsolvedSchool.getPupilList().get(4).setFriend(unsolvedSchool.getPupilList().get(5));
        unsolvedSchool.getPupilList().get(5).setFriend(unsolvedSchool.getPupilList().get(4));
        unsolvedSchool.getPupilList().get(6).setFriend(unsolvedSchool.getPupilList().get(4));
        unsolvedSchool.getPupilList().get(7).setFriend(unsolvedSchool.getPupilList().get(0));
        unsolvedSchool.getPupilList().get(8).setFriend(unsolvedSchool.getPupilList().get(9));
        unsolvedSchool.getPupilList().get(9).setFriend(unsolvedSchool.getPupilList().get(10));
        unsolvedSchool.getPupilList().get(10).setFriend(unsolvedSchool.getPupilList().get(8));
        unsolvedSchool.getPupilList().get(0).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(1).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(2).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(3).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(4).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(5).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        unsolvedSchool.getPupilList().get(6).setFixedSchoolClass(unsolvedSchool.getSchoolClasses().get(0));
        return unsolvedSchool;
    }

    private static List<Pupil> generatePupils() {
        List<Pupil> pupils = new ArrayList<>();
        pupils.add(new Pupil(1, "Alina", Gender.F));
        pupils.add(new Pupil(2, "Bernadette", Gender.F));
        pupils.add(new Pupil(3, "Clara", Gender.F));
        pupils.add(new Pupil(4, "Diana", Gender.F));
        pupils.add(new Pupil(5, "Erik", Gender.M));
        pupils.add(new Pupil(6, "Florian", Gender.M));
        pupils.add(new Pupil(7, "Gunther", Gender.M));
        pupils.add(new Pupil(8, "Hagen", Gender.M));
        pupils.add(new Pupil(9, "Ida", Gender.F));
        pupils.add(new Pupil(10, "Jette", Gender.F));
        pupils.add(new Pupil(11, "Karl", Gender.M));
        pupils.add(new Pupil(12, "Leif", Gender.M));
        return pupils;
    }
}
