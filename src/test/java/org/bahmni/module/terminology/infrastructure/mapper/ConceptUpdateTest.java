package org.bahmni.module.terminology.infrastructure.mapper;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class ConceptUpdateTest extends BaseModuleWebContextSensitiveTest {
    @Autowired
    private ConceptUpdate conceptUpdate;

    @Autowired
    private ConceptService conceptService;

    @Before
    public void setUp() throws Exception {
        executeDataSet("conceptUpdateTestDS.xml");
    }

    @Test
    public void shouldRemoveAllSetMembers() throws Exception {
        Concept newConcept = conceptService.getConcept(600);
        Concept existingConcept = conceptService.getConcept(401);
        assertEquals(3, existingConcept.getSetMembers().size());
        assertEquals(0, newConcept.getSetMembers().size());
        Concept updatedConcept = conceptUpdate.merge(existingConcept, newConcept);
        assertNotNull(updatedConcept);
        assertEquals(0, updatedConcept.getSetMembers().size());
    }

    @Test
    public void shouldRemoveSetMembersNotPresentInNewConcept() throws Exception {
        Concept newConcept = conceptService.getConcept(601);
        Concept existingConcept = conceptService.getConcept(401);
        assertEquals(3, existingConcept.getSetMembers().size());
        assertEquals(2, newConcept.getSetMembers().size());
        Concept updatedConcept = conceptUpdate.merge(existingConcept, newConcept);
        assertNotNull(updatedConcept);
        assertEquals(2, updatedConcept.getSetMembers().size());
        assertFalse(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(502)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(503)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(505)));
    }

    @Test
    public void shouldAddSetMembersPresentInNewConcept() throws Exception {
        Concept newConcept = conceptService.getConcept(601);
        Concept existingConcept = conceptService.getConcept(402);
        assertEquals(0, existingConcept.getSetMembers().size());
        assertEquals(2, newConcept.getSetMembers().size());
        Concept updatedConcept = conceptUpdate.merge(existingConcept, newConcept);
        assertNotNull(updatedConcept);
        assertEquals(2, updatedConcept.getSetMembers().size());
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(503)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(505)));

    }

    @Test
    public void shouldNotRemoveLocalSetMembers() throws Exception {
        Concept newConcept = conceptService.getConcept(602);
        Concept existingConcept = conceptService.getConcept(401);
        assertEquals(3, existingConcept.getSetMembers().size());
        assertEquals(3, newConcept.getSetMembers().size());
        Concept updatedConcept = conceptUpdate.merge(existingConcept, newConcept);
        assertNotNull(updatedConcept);
        assertEquals(3, updatedConcept.getSetMembers().size());
        assertFalse(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(502)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(503)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(505)));
        assertTrue(findSetMember(updatedConcept.getSetMembers(), conceptService.getConcept(506)));

    }

    private boolean findSetMember(List<Concept> setMembers, Concept concept) {
        for (Concept member : setMembers) {
            if (member.getUuid().equals(concept.getUuid())) {
                return true;
            }
        }
        return false;
    }
}