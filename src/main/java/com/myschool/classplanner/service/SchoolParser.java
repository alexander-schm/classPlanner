package com.myschool.classplanner.service;

import com.myschool.classplanner.model.School;

public interface SchoolParser {

    School fetchData(String classSourceIdentifier, String pupilSourceIdentifier);

}
