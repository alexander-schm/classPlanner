package com.myschool.classplanner.service;

import com.myschool.classplanner.model.School;

public interface SchoolWriter {

    void writeSolution(School school, String destinationIdentifier);

    void write(String content, String destinationIdentifier);

}
