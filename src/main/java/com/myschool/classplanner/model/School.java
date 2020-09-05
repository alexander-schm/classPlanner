package com.myschool.classplanner.model;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class School {

    private List<SchoolClass> classList = new ArrayList<>();
    private List<Pupil> pupilList = new ArrayList<>();
    private HardSoftLongScore score;

    @ValueRangeProvider(id = "schoolClassRange")
    @ProblemFactCollectionProperty
    public List<SchoolClass> getSchoolClasses() {
        return classList;
    }
    @PlanningEntityCollectionProperty
    public List<Pupil> getPupilList() {
        return pupilList;
    }

    public void setPupilList(List<Pupil> pupilList) {
        this.pupilList = pupilList;
    }

    @PlanningScore
    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }
}
