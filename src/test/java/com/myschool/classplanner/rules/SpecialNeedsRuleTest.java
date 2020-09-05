package com.myschool.classplanner.rules;

import com.myschool.classplanner.model.Gender;
import com.myschool.classplanner.model.Pupil;
import com.myschool.classplanner.model.SchoolClass;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests individual rules without running the solver.
 */
public class SpecialNeedsRuleTest {

    private KieSession session;
    private HardSoftLongScoreHolder scoreHolder;

    private Pupil pupilWithSpecialNeeds;
    private Pupil anotherPupilWithSpecialNeeds;
    private Pupil standardPupil;

    private SchoolClass schoolClass;
    private SchoolClass anotherSchoolClass;

    @Before
    public void setUp() {
        session = prepareDroolsSession("rules/classPlannerRules.drl");
        scoreHolder = new HardSoftLongScoreHolder(true);
        session.setGlobal("scoreHolder", scoreHolder);
        schoolClass = new SchoolClass(1, "1");
        anotherSchoolClass = new SchoolClass(2, "2");
        pupilWithSpecialNeeds = new Pupil(1, "male1", Gender.M, true);
        anotherPupilWithSpecialNeeds = new Pupil(2, "male2", Gender.M);
        standardPupil = new Pupil(3, "male3", Gender.M);
    }

    private void addToSession() {
        session.insert(pupilWithSpecialNeeds);
        session.insert(anotherPupilWithSpecialNeeds);
        session.insert(standardPupil);
        session.insert(schoolClass);
    }

    @Test
    public void distributionOfSpecialNeedPupilsIncreaseScore() {
        pupilWithSpecialNeeds.setSchoolClass(schoolClass);
        anotherPupilWithSpecialNeeds.setSchoolClass(schoolClass);
        addToSession();

        int rulesFired = session.fireAllRules(match -> "specialNeeds".equals(match.getRule().getName()));
        assertEquals(1, rulesFired);
        long scorePupilsSameClass = scoreHolder.getSoftScore();

        setUp();
        pupilWithSpecialNeeds.setSchoolClass(schoolClass);
        anotherPupilWithSpecialNeeds.setSchoolClass(anotherSchoolClass);
        session.fireAllRules(match -> "specialNeeds".equals(match.getRule().getName()));
        assertTrue(scoreHolder.getSoftScore() > scorePupilsSameClass);
    }

    private KieSession prepareDroolsSession(String droolsFile) {
        Resource classPathResource = ResourceFactory.newClassPathResource(droolsFile);
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(classPathResource, ResourceType.DRL);
        if (knowledgeBuilder.hasErrors()) {
            throw new IllegalStateException("KnowledgeBuilder has errors: " + knowledgeBuilder.getErrors().toString());
        }
        return knowledgeBuilder.newKieBase().newKieSession();
    }
}
