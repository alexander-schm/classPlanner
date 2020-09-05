package com.myschool.classplanner.service;

import com.myschool.classplanner.model.School;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import static com.myschool.classplanner.service.SchoolSolver.LINE_BREAK;
import static javax.management.timer.Timer.ONE_MINUTE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SchoolSolverTest {

    static public String CLASSES_FILE = "src/test/SchoolClassTestData.csv";
    static public String PUPILS_FILE = "src/test/PupilTestData.csv";

    private SchoolSolver schoolSolver;

    @Before
    public void setup() {
        schoolSolver = new SchoolSolver("rules/solverConfig.xml");
        schoolSolver.getSolverConfig().withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(3L));
    }

    @Test
    public void testSolve() {
        School unsolvedSchool = ExampleFactory.createSimpleExample();
        assertNull(unsolvedSchool.getScore());
        School solvedSchool = schoolSolver.solve(unsolvedSchool);
        assertNotNull(solvedSchool.getScore());
    }

    @Test
    public void testGenerateSummary() {
        School unsolvedSchool = ExampleFactory.createSimpleExample();
        String summary = schoolSolver.generateSummary(unsolvedSchool);
        assertTrue(summary.length() > 50);
        assertTrue(summary.startsWith("Solution Summary" + LINE_BREAK));
    }

    @Test
    public void testGenerateAssignments() {
        School unsolvedSchool = ExampleFactory.createSimpleExample();
        String assignments = schoolSolver.generateAssignments(unsolvedSchool);
        assertTrue(assignments.length() > 50);
        assertTrue(assignments.startsWith("Assignments" + LINE_BREAK));
    }

    @Test
    public void testGenerateClassAssignments() {
        School unsolvedSchool = ExampleFactory.createSimpleExample();
        String classAssignments = schoolSolver.generateClassAssignments(unsolvedSchool);
        System.out.println(classAssignments);
        assertTrue(classAssignments.length() > 50);
        assertTrue(classAssignments.startsWith("Assignments per Class" + LINE_BREAK));
    }

    // Test to run assignment manually
    @Ignore
    @Test
    public void readFilesAndWriteSolution() {
        // Init solver config
        final long maxMillisecondsToSpentOnSolutionFinding = ONE_MINUTE * 10;
        schoolSolver.getSolverConfig().withTerminationConfig(new TerminationConfig().withMillisecondsSpentLimit(maxMillisecondsToSpentOnSolutionFinding));

        // Init In/Out
        FileIO csvParser = new FileIO();

        // Solve
        School solvedSchool = schoolSolver.solve(csvParser.fetchData(CLASSES_FILE, PUPILS_FILE));

        // Report solution
        System.out.println(schoolSolver.generateSummary(solvedSchool));
        System.out.println(solvedSchool.getPupilList().toString());
        csvParser.write(schoolSolver.generateSummary(solvedSchool), "summary.txt");
        csvParser.writeSolution(solvedSchool, "solution.csv");
        csvParser.write(schoolSolver.generateAssignments(solvedSchool), "assignments.txt");
        csvParser.write(schoolSolver.generateClassAssignments(solvedSchool), "classAssignments.txt");
    }
}