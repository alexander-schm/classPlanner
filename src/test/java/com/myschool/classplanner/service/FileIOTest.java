package com.myschool.classplanner.service;

import com.myschool.classplanner.model.School;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileIOTest {

    static public String CLASSES_FILE = "src/test/resources/SchoolClassTestData.csv";
    static public String PUPILS_FILE = "src/test/resources/PupilTestData.csv";
    static public String TMP_FILE = "src/test/resources/TmpFile.txt";

    private FileIO fileIO;

    @Before
    public void setup() {
        fileIO = new FileIO();
    }

    @Test
    public void testFetchData() {
        School school = fileIO.fetchData(CLASSES_FILE, PUPILS_FILE);
        assertEquals(5, school.getSchoolClasses().size());
        assertEquals(24, school.getPupilList().size());
    }

    @Test
    public void testWrite() {
        try {
            String content = "lorem ipsum dolor sit amet";
            fileIO.write(content, TMP_FILE);
            File summaryHandler = new File(TMP_FILE);
            assertTrue(summaryHandler.isFile());
            assertEquals(content.length(), summaryHandler.length());
        } finally {
            new File(TMP_FILE).delete();
        }
    }

    @Test
    public void testWriteSolution() throws FileNotFoundException {
        School school = ExampleFactory.createSimpleExample();
        try {
            fileIO.writeSolution(school, TMP_FILE);
            File fileHandler = new File(TMP_FILE);
            assertTrue(fileHandler.isFile());
            assertTrue(fileHandler.length() > 100);
            Scanner scanner = new Scanner(fileHandler);
            assertEquals("ID;Name;Geschlecht;Klasse;Freund;Besonderheit;Neue Klasse;Klasse korrekt;Freund korrekt;Freund2 korrekt;Freund3 korrekt", scanner.nextLine());
            scanner.close();
        } finally {
            new File(TMP_FILE).delete();
        }
    }

}