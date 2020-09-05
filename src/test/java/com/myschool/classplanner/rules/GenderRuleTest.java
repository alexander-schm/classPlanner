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
public class GenderRuleTest {

    private KieSession session;
    private HardSoftLongScoreHolder scoreHolder;

    private Pupil male;
    private Pupil anotherMale;
    private Pupil yetAnotherMale;
    private Pupil female;
    private Pupil anotherFemale;

    private SchoolClass schoolClass;

    @Before
    public void setUp() {
        session = prepareDroolsSession("rules/classPlannerRules.drl");
        scoreHolder = new HardSoftLongScoreHolder(true);
        session.setGlobal("scoreHolder", scoreHolder);
        schoolClass = new SchoolClass(1, "1");
        male = new Pupil(1, "male1", Gender.M);
        anotherMale= new Pupil(2, "male2", Gender.M);
        yetAnotherMale = new Pupil(3, "male3", Gender.M);
        female = new Pupil(4, "female1", Gender.F);
        anotherFemale = new Pupil(5, "female2", Gender.F);
    }

    private void addToSession() {
        session.insert(male);
        session.insert(anotherMale);
        session.insert(yetAnotherMale);
        session.insert(female);
        session.insert(anotherFemale);
        session.insert(schoolClass);
    }

    @Test
    public void twoMalesHaveWorseScoreThanOneMaleAndOneFemale() {
        // when:
        // a class with two pupils and equal gender distribution
        male.setSchoolClass(schoolClass);
        female.setSchoolClass(schoolClass);
        addToSession();

        int rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(2, rulesFired);
        long scoreEqualDistribution = scoreHolder.getSoftScore();

        // then: class with same amount of pupils but no equal gender distribution
        setUp();
        male.setSchoolClass(schoolClass);
        anotherMale.setSchoolClass(schoolClass);
        addToSession();
        rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(1, rulesFired);
        assertTrue(scoreHolder.getSoftScore() < scoreEqualDistribution);
    }

    @Test
    public void twoFemalesHaveWorseScoreThanOneMaleAndOneFemale() {
        // when:
        // a class with two pupils and equal gender distribution
        male.setSchoolClass(schoolClass);
        female.setSchoolClass(schoolClass);
        addToSession();

        int rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(2, rulesFired);
        long scoreEqualDistribution = scoreHolder.getSoftScore();

        // then: class with same amount of pupils but no equal gender distribution
        setUp();
        female.setSchoolClass(schoolClass);
        anotherFemale.setSchoolClass(schoolClass);
        addToSession();
        rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(1, rulesFired);
        assertTrue(scoreHolder.getSoftScore() < scoreEqualDistribution);
    }

    @Test
    public void threeMalesHaveWorseScoreThanTwoMalesAndOneFemale() {
        // when:
        // a class with two pupils and equal gender distribution
        male.setSchoolClass(schoolClass);
        anotherMale.setSchoolClass(schoolClass);
        female.setSchoolClass(schoolClass);
        addToSession();

        int rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(2, rulesFired);
        long scoreEqualDistribution = scoreHolder.getSoftScore();

        // then: class with same amount of pupils but no equal gender distribution
        setUp();
        male.setSchoolClass(schoolClass);
        anotherMale.setSchoolClass(schoolClass);
        yetAnotherMale.setSchoolClass(schoolClass);
        addToSession();
        rulesFired = session.fireAllRules(match ->
                "females".equals(match.getRule().getName()) || "males".equals(match.getRule().getName()));
        assertEquals(1, rulesFired);
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
