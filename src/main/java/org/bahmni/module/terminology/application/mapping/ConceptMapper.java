package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.factory.ConceptFactory;
import org.bahmni.module.terminology.application.model.ConceptDescriptionRequest;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.*;
import org.openmrs.api.ConceptNameType;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component(value = "TRClientConceptMapper")
public class ConceptMapper {

    public static final String TERMINOLOGY_SERVICES_VERSION_PREFIX = "TS-";
    private ConceptService conceptService;
    private ConceptNameMapper conceptNameMapper;
    private ConceptReferenceTermMapper conceptReferenceTermMapper;
    private IdMappingsRepository idMappingsRepository;
    private ConceptFactory conceptFactory;

    @Autowired
    public ConceptMapper(ConceptService conceptService,
                         ConceptNameMapper conceptNameMapper,
                         ConceptReferenceTermMapper conceptReferenceTermMapper,
                         IdMappingsRepository idMappingsRepository,
                         ConceptFactory conceptFactory) {
        this.conceptService = conceptService;
        this.conceptNameMapper = conceptNameMapper;
        this.conceptReferenceTermMapper = conceptReferenceTermMapper;
        this.idMappingsRepository = idMappingsRepository;
        this.conceptFactory = conceptFactory;
    }

    public Concept map(ConceptRequest conceptRequest) {
        Concept concept = conceptFactory.createConcept(conceptRequest);
        concept.setSet(conceptRequest.isSet());
        concept.setRetired(conceptRequest.isRetired());
        concept.setRetireReason(conceptRequest.getRetireReason());
        concept.setVersion(TERMINOLOGY_SERVICES_VERSION_PREFIX + conceptRequest.getVersion());
        mapConceptDatatype(concept, conceptRequest);
        mapConceptClass(concept, conceptRequest);
        concept.addDescription(mapDescriptions(conceptRequest.getConceptDescriptionRequest()));
        setConceptNames(concept, conceptRequest);
        //concept.setFullySpecifiedName(conceptNameMapper.map(conceptRequest.getFullySpecifiedName()));
        concept.setConceptMappings(conceptReferenceTermMapper.map(conceptRequest.getConceptReferenceTermRequests()));
        mapConceptAnswers(concept, conceptRequest);
        mapSetMembers(concept, conceptRequest);
        return concept;
    }

    private void setConceptNames(Concept concept, ConceptRequest conceptRequest) {
        Set<ConceptName> names = conceptNameMapper.map(conceptRequest.getConceptNameRequests());
        ConceptName fullySpecifiedName = null;
        for (ConceptName name : names) {
            if (name.getConceptNameType() != null &&
                    name.getConceptNameType().equals(ConceptNameType.FULLY_SPECIFIED)) {
                fullySpecifiedName = name;
            }
        }
        if (fullySpecifiedName == null) {
            throw new RuntimeException("Concept must define a FULLY_SPECIFIED name.");
        }
        concept.addName(fullySpecifiedName);
        names.remove(fullySpecifiedName);

        for (ConceptName name : names) {
            concept.addName(name);
        }
    }

    private void mapConceptAnswers(Concept concept, ConceptRequest conceptRequest) {
        if (null != conceptRequest.getConceptAnswers()) {
            for (String conceptAnswerUuid : conceptRequest.getConceptAnswers()) {
                IdMapping idMap = idMappingsRepository.findByExternalId(conceptAnswerUuid);
                if (idMap == null) {
                    throw new RuntimeException("Can not identify concept answer with external Id:" + conceptAnswerUuid);
                }
                String internalId = idMap.getInternalId();
                ConceptAnswer answer = createConceptAnswer(internalId);
                concept.addAnswer(answer);
            }
        }
    }

    private ConceptAnswer createConceptAnswer(String internalId) {
        ConceptAnswer conceptAnswer = new ConceptAnswer();
        Concept conceptByUuid = conceptService.getConceptByUuid(internalId);
        if (null != conceptByUuid) {
            conceptAnswer.setAnswerConcept(conceptByUuid);
            return conceptAnswer;
        }
        Drug drugByUuid = conceptService.getDrugByUuid(internalId);
        conceptAnswer.setAnswerConcept(drugByUuid.getConcept());
        conceptAnswer.setAnswerDrug(drugByUuid);
        return conceptAnswer;
    }

    private void mapSetMembers(Concept concept, ConceptRequest conceptRequest) {
        if (null != conceptRequest.getSetMembers()) {
            for (String setMemberUuid : conceptRequest.getSetMembers()) {
                IdMapping mapping = idMappingsRepository.findByExternalId(setMemberUuid);
                if (null != mapping) {
                    concept.addSetMember(conceptService.getConceptByUuid(mapping.getInternalId()));
                } else {
                    throw new RuntimeException("Can not identify concept set member with external id: " + setMemberUuid);
                }
            }
        }
    }

    private ConceptDescription mapDescriptions(ConceptDescriptionRequest conceptDescriptionRequest) {
        if (null == conceptDescriptionRequest) {
            return null;
        }
        ConceptDescription description = new ConceptDescription();
        description.setDescription(conceptDescriptionRequest.getDescription());
        description.setLocale(Locale.ENGLISH);
        return description;
    }

    private void mapConceptClass(Concept concept, ConceptRequest conceptRequest) {
        String conceptClassName = conceptRequest.getConceptClass();
        ConceptClass conceptClass = conceptService.getConceptClassByName(conceptClassName);
        if (conceptClass == null) {
            conceptClass = new ConceptClass();
            conceptClass.setName(conceptClassName);
            conceptClass.setDescription(String.format("%s - synced from registry", conceptClassName));
            conceptService.saveConceptClass(conceptClass);
        }
        concept.setConceptClass(conceptClass);
    }

    private void mapConceptDatatype(Concept concept, ConceptRequest request) {
        if (isNotBlank(request.getDatatypeName())) {
            ConceptDatatype datatype = conceptService.getConceptDatatypeByName(request.getDatatypeName());
            concept.setDatatype(datatype);
            if (datatype.isNumeric()) {
                ConceptNumeric conceptNumeric = ((ConceptNumeric) concept);
                conceptNumeric.setHiAbsolute(request.getAbsoluteHigh());
                conceptNumeric.setHiCritical(request.getCriticalHigh());
                conceptNumeric.setHiNormal(request.getNormalHigh());
                conceptNumeric.setLowNormal(request.getNormalLow());
                conceptNumeric.setLowCritical(request.getCriticalLow());
                conceptNumeric.setLowAbsolute(request.getAbsoluteLow());
                conceptNumeric.setUnits(request.getUnits());
                conceptNumeric.setPrecise(request.getPrecise());
            }
        }
    }
}
