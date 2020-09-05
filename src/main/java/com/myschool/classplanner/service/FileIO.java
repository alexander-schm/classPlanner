package com.myschool.classplanner.service;

import com.myschool.classplanner.model.Gender;
import com.myschool.classplanner.model.Pupil;
import com.myschool.classplanner.model.School;
import com.myschool.classplanner.model.SchoolClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class FileIO implements SchoolParser, SchoolWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileIO.class);

    static public Character DELIMITER = ';';

    private List<SchoolClass> getClasses(String fileName) {
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(DELIMITER).parse(in);
            List<SchoolClass> classes = new ArrayList<>();
            for (CSVRecord record : records) {
                if (record.get(0).chars().allMatch(Character::isDigit)) {
                    classes.add(new SchoolClass(parseInt(record.get(0)), record.get(1)));
                } else {
                    LOGGER.debug("Skipping record {} because of non-numeric ID", record.getRecordNumber());
                }
            }
            return classes;
        } catch (FileNotFoundException e) {
            LOGGER.warn("File not found: {}", fileName, e);
            return null;
        } catch (IOException e) {
            LOGGER.warn("IO Exception", e);
            return null;
        }
    }

    private Map<Integer, Pupil> getPupils(String pupilFileName, List<SchoolClass> classes) {
        try {
            Reader in = new FileReader(pupilFileName);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(DELIMITER).parse(in);
            Map<Integer, Pupil> pupils = new HashMap<>();
            List<CSVRecord> recordsWithFriend = new ArrayList<>();
            for (CSVRecord record : records) {
                if (record.get(0).chars().allMatch(Character::isDigit)) {
                    Pupil addedPupil = new Pupil(parseInt(record.get(0)), record.get(1), Gender.valueOf(record.get(2).toUpperCase()));
                    if (record.size() > 3 && !record.get(3).isEmpty()) {
                        SchoolClass schoolClass = classes.get(parseInt(record.get(3)) - 1);
                        addedPupil.setFixedSchoolClass(schoolClass);
                        addedPupil.setSchoolClass(schoolClass);
                    }
                    pupils.put(addedPupil.getId(), addedPupil);
                    if (record.size() > 4 && !record.get(4).isEmpty()) {
                        addedPupil.setSpecialNeeds(parseInt(record.get(4)) > 0);
                    }
                    if (record.size() > 5 && !record.get(5).isEmpty()
                            || record.size() > 6 && !record.get(6).isEmpty()
                            || record.size() > 7 && !record.get(7).isEmpty()) {
                        recordsWithFriend.add(record);
                    }
                }
            }
            for (CSVRecord record : recordsWithFriend) {
                if (record.size() > 5 && !record.get(5).isEmpty()) { pupils.get(parseInt(record.get(0))).setFriend(pupils.get(parseInt(record.get(5)))); }
                if (record.size() > 6 && !record.get(6).isEmpty()) { pupils.get(parseInt(record.get(0))).setSecondaryFriend(pupils.get(parseInt(record.get(6)))); }
                if (record.size() > 7 && !record.get(7).isEmpty()) { pupils.get(parseInt(record.get(0))).setTertiaryFriend(pupils.get(parseInt(record.get(7)))); }
            }
            return pupils;
        } catch (FileNotFoundException e) {
            LOGGER.warn("File not found: {}", pupilFileName, e);
            return null;
        } catch (IOException e) {
            LOGGER.warn("IO Exception", e);
            return null;
        }

    }

    public School fetchData(String classFileName, String pupilFileName) {
        List<SchoolClass> classes = getClasses(classFileName);
        Map<Integer, Pupil> pupils = getPupils(pupilFileName, classes);
        School school = new School();
        school.getSchoolClasses().addAll(classes);
        school.getPupilList().addAll(pupils.values());
        LOGGER.debug("classes {}", school.getSchoolClasses());
        LOGGER.debug("pupils {}", school.getPupilList());
        return school;
    }

    public void write(String content, String fileName) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public void writeSolution(School school, String fileName) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Name", "Geschlecht", "Klasse", "Freund", "Besonderheit", "Neue Klasse", "Klasse korrekt", "Freund korrekt", "Freund2 korrekt", "Freund3 korrekt")
                .withDelimiter(DELIMITER));
            for (Pupil pupil : school.getPupilList()) {
                csvPrinter.printRecord(pupil.getId(),
                        pupil.getName(),
                        pupil.getGender().toString(),
                        (pupil.getFixedSchoolClass() == null) ? "" : pupil.getFixedSchoolClass().getId(),
                        (pupil.getFriend() == null) ? "" : pupil.getFriend().getId(),
                        pupil.getSpecialNeeds() ? "1" : "0",
                        pupil.getSchoolClass() == null ? "null" : pupil.getSchoolClass().toString(),
                        pupil.getFixedSchoolClass() == null || pupil.getFixedSchoolClass() == pupil.getSchoolClass(),
                        pupil.getFriend() == null || pupil.getFriend().getSchoolClass() == pupil.getSchoolClass(),
                        pupil.getSecondaryFriend() == null || pupil.getSecondaryFriend().getSchoolClass() == pupil.getSchoolClass(),
                        pupil.getTertiaryFriend() == null || pupil.getTertiaryFriend().getSchoolClass() == pupil.getSchoolClass()
                        );
            }
            csvPrinter.close(true);
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }
}
