package com.myschool.classplanner.service;

import com.myschool.classplanner.model.Gender;
import com.myschool.classplanner.model.Pupil;
import com.myschool.classplanner.model.School;
import com.myschool.classplanner.model.SchoolClass;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.StrictMath.toIntExact;

public class SchoolSolver {

    static String LINE_BREAK = "\r\n";
    static String SEPARATOR_LINE = "+--------------------------------------------------+" + LINE_BREAK;

    final private SolverConfig solverConfig;

    public SchoolSolver(String solverConfigLocation) {
        solverConfig = SolverConfig.createFromXmlResource(solverConfigLocation);
    }

    public SolverConfig getSolverConfig() { return solverConfig; }

    public School solve(School unsolvedSchool) {
        SolverFactory<School> solverFactory = SolverFactory.create(solverConfig);
        Solver<School> solver = solverFactory.buildSolver();

        return solver.solve(unsolvedSchool);

    }

    public String generateSummary(School school) {
        StringBuilder summary = new StringBuilder();
        summary.append("Solution Summary").append(LINE_BREAK);
        summary.append(String.format("Solver Score: %s", school.getScore())).append(LINE_BREAK);
        for (SchoolClass schoolClass : school.getSchoolClasses()) {
            int amountOfFriendViolations = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(pupil -> pupil.getFriend() != null && pupil.getFriend().getSchoolClass() != schoolClass).count());
            int amountOfSecondaryFriendViolations = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(pupil -> pupil.getSecondaryFriend() != null && pupil.getSecondaryFriend().getSchoolClass() != schoolClass).count());
            int amountOfTertiaryFriendViolations = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(pupil -> pupil.getTertiaryFriend() != null && pupil.getTertiaryFriend().getSchoolClass() != schoolClass).count());
            int amountOfPupils = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).count());
            int amountOfMales = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(pupil -> pupil.getGender() == Gender.M).count());
            int amountOfFemales = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(pupil -> pupil.getGender() == Gender.F).count());
            int amountOfSpecialNeeds = toIntExact(school.getPupilList().stream().filter(pupil -> schoolClass.equals(pupil.getSchoolClass())).filter(Pupil::getSpecialNeeds).count());
            summary.append(String.format("Class %s has %2d pupils (%dm/%df), with %d pupils with special needs. Rule violations: %d friend (%d secondary, %d tertiary)",
                    schoolClass.toString(),
                    amountOfPupils,
                    amountOfMales,
                    amountOfFemales,
                    amountOfSpecialNeeds,
                    amountOfFriendViolations,
                    amountOfSecondaryFriendViolations,
                    amountOfTertiaryFriendViolations)).append(LINE_BREAK);
        }
        return summary.toString();
    }

    public String generateAssignments(School school) {
        StringBuilder assignments = new StringBuilder();
        assignments.append("Assignments").append(LINE_BREAK);
        assignments.append(SEPARATOR_LINE);
        for (Pupil p : school.getPupilList()) {
            assignments.append(formatPupil(p));
        }
        return assignments.toString();
    }

    private String formatPupil(Pupil pupil) {
        String className = (pupil.getSchoolClass() == null) ? "none" : pupil.getSchoolClass().toString();
        if (pupil.getFixedSchoolClass() != null) {
            className += "*";
        }
        String pupilEntry = String.format("| %25s | %20s |" + LINE_BREAK, pupil.getName(), className);
        pupilEntry += SEPARATOR_LINE;
        return pupilEntry;
    }

    public String generateClassAssignments(School school) {
        StringBuilder schoolAssignments = new StringBuilder();
        schoolAssignments.append("Assignments per Class").append(LINE_BREAK);
        for (SchoolClass s : school.getSchoolClasses()) {
            schoolAssignments.append(formatSchoolClass(s, school.getPupilList().stream().filter(pupil -> s.equals(pupil.getSchoolClass())).collect(Collectors.toList())));
        }
        return schoolAssignments.toString();
    }

    private String formatSchoolClass(SchoolClass schoolClass, List<Pupil> pupilsOfSchoolClass) {
        StringBuilder schoolClassEntry = new StringBuilder();
        schoolClassEntry.append(LINE_BREAK).append(schoolClass.toString());
        schoolClassEntry.append(LINE_BREAK).append(SEPARATOR_LINE);
        for (Pupil p : pupilsOfSchoolClass) {
            schoolClassEntry.append(formatPupil(p));
        }
        return schoolClassEntry.toString();
    }
}
