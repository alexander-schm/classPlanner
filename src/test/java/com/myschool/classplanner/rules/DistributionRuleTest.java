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

import static org.junit.Assert.assertTrue;

/**
 * This class tests individual rules without running the solver.
 */
public class DistributionRuleTest {

    private KieSession session;
    private HardSoftLongScoreHolder scoreHolder;

    private Pupil male1;
    private Pupil male2;
    private Pupil male3;
    private Pupil female1;
    private Pupil female2;

    private SchoolClass schoolClass1;
    private SchoolClass schoolClass2;

    @Before
    public void setUp() {
        session = prepareDroolsSession("rules/classPlannerRules.drl");
        scoreHolder = new HardSoftLongScoreHolder(true);
        session.setGlobal("scoreHolder", scoreHolder);
        schoolClass1 = new SchoolClass(1, "1");
        schoolClass2 = new SchoolClass(2, "2");
        male1 = new Pupil(1, "male1", Gender.M);
        male2 = new Pupil(2, "male2", Gender.M);
        male3 = new Pupil(3, "male3", Gender.M);
        female1 = new Pupil(4, "female1", Gender.F);
        female2 = new Pupil(5, "female2", Gender.F);
    }

    private void addToSession() {
        session.insert(male1);
        session.insert(male2);
        session.insert(male3);
        session.insert(female1);
        session.insert(female2);
        session.insert(schoolClass1);
        session.insert(schoolClass2);
    }

    @Test
    public void twoClassesHaveBestScoreIfPupilsAreEvenlyDistributed() {
        // when:
        // a class with two pupils and equal gender distribution
        male1.setSchoolClass(schoolClass1);
        male2.setSchoolClass(schoolClass2);
        addToSession();

        session.fireAllRules(match -> "equalClasses".equals(match.getRule().getName()));
        long scoreEqualDistribution = scoreHolder.getSoftScore();

        // then: class with same amount of pupils but no equal gender distribution
        setUp();
        male1.setSchoolClass(schoolClass1);
        male2.setSchoolClass(schoolClass1);
        addToSession();
        session.fireAllRules(match -> "equalClasses".equals(match.getRule().getName()));
        assertTrue(scoreHolder.getSoftScore() < scoreEqualDistribution);
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
