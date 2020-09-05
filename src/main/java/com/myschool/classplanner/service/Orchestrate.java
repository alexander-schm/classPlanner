package com.myschool.classplanner.service;

import com.myschool.classplanner.model.School;

public class Orchestrate {

    static public void readFromAndWriteToDisc() {
        final String SOURCE_CLASS_FILE_NAME = "classes.csv";
        final String SOURCE_PUPILS_FILE_NAME = "pupils.csv";
        final String TARGET_SUMMARY_FILE_NAME = "summary.txt";
        final String TARGET_ASSIGNMENT_FILE_NAME = "assignments.txt";
        final String TARGET_CLASS_ASSIGNMENT_FILE_NAME = "classAssignments.txt";
        final String TARGET_SOLUTION_FILE_NAME = "solution.csv";
        SchoolSolver schoolSolver = new SchoolSolver("rules/solverConfig.xml");
        FileIO fileIO = new FileIO();
        School solvedSchool = schoolSolver.solve(fileIO.fetchData(SOURCE_CLASS_FILE_NAME, SOURCE_PUPILS_FILE_NAME));
        fileIO.write(schoolSolver.generateSummary(solvedSchool), TARGET_SUMMARY_FILE_NAME);
        fileIO.write(schoolSolver.generateAssignments(solvedSchool), TARGET_ASSIGNMENT_FILE_NAME);
        fileIO.write(schoolSolver.generateClassAssignments(solvedSchool), TARGET_CLASS_ASSIGNMENT_FILE_NAME);
        fileIO.writeSolution(solvedSchool, TARGET_SOLUTION_FILE_NAME);
    }
}
