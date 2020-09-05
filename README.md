# classPlanner

Tool to assign pupils to classes, given certain constraints formulated in Drools Rules Language.

## Prerequisites

To build and run the software you need
```
maven 3
jdk 8
```

### Usage

Place pupil file 'pupils.csv' and school class file 'classes.csv' next to the jar file and run the jar
Solving time is configured to 1 hour.

Example files
```
pupils.csv -> src/test/resources/PupilTestData.csv
classes.csv -> src/test/resources/SchoolClassTestData.csv
```

## Solver configuration

`Rules` src/main/resources/rules/classPlannerRules.drools

`Configuration` src/main/resources/rules/solverConfig.xml


## Technologies

* [Spring Boot](https://projects.spring.io/spring-boot/) - Microservice framework
* [Maven](https://maven.apache.org/) - Dependency management
* [Optaplanner](https://www.optaplanner.org/) - AI constraint solver