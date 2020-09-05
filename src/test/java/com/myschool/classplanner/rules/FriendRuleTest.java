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
public class FriendRuleTest {

    private KieSession session;
    private HardSoftLongScoreHolder scoreHolder;

    private Pupil pupil;
    private Pupil friend;

    private SchoolClass pupilsSchoolClass;
    private SchoolClass otherSchoolClass;

    @Before
    public void setUp() {
        session = prepareDroolsSession("rules/classPlannerRules.drl");
        scoreHolder = new HardSoftLongScoreHolder(true);
        session.setGlobal("scoreHolder", scoreHolder);
        pupilsSchoolClass = new SchoolClass(1, "1");
        otherSchoolClass = new SchoolClass(2, "2");
        pupil = new Pupil(1, "Pupil", Gender.M);
        pupil.setSchoolClass(pupilsSchoolClass);
        friend = new Pupil(1, "Friend", Gender.F);
        pupil.setFriend(friend);
    }

    private void addToSession() {
        session.insert(pupil);
        session.insert(friend);
        session.insert(pupilsSchoolClass);
        session.insert(otherSchoolClass);
    }

    @Test
    public void friendInOtherClassDoesReduceScore() {
        // when:
        // pupil and friend have different classes
        friend.setSchoolClass(otherSchoolClass);
        addToSession();

        // then: at least one rule is violated
        int rulesFired = session.fireAllRules(match -> "friends".equals(match.getRule().getName()));
        assertEquals(1, rulesFired);
        assertTrue(scoreHolder.getSoftScore() < 0);
    }

    @Test
    public void friendInSameClassDoesNotReduceScore() {
        // when:
        // pupil and friend have same classes
        friend.setSchoolClass(pupilsSchoolClass);
        addToSession();

        // then: no rule is violated
        int rulesFired = session.fireAllRules(match -> "friends".equals(match.getRule().getName()));
        assertEquals(0, rulesFired);
        assertEquals(0, scoreHolder.getSoftScore());
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
