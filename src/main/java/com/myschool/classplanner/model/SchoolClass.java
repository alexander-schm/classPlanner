package com.myschool.classplanner.model;

public class SchoolClass {

    private Integer id;
    private String name;

    public SchoolClass(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }

    public String toString() {
        return name;
    }
}
