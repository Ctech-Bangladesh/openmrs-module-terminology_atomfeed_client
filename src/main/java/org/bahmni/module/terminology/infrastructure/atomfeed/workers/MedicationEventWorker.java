package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.CodeableConcept;
import org.bahmni.module.terminology.application.model.Coding;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.application.model.ResourceExtension;
import org.bahmni.module.terminology.application.model.drug.Medication;
import org.bahmni.module.terminology.application.model.drug.MedicationProduct;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.bahmni.module.terminology.util.StringRegexUtils;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;

import java.util.List;

import static java.lang.String.format;

public class MedicationEventWorker implements EventWorker {
    private static Logger logger = Logger.getLogger(MedicationEventWorker.class);


    private TRFeedProperties properties;
    private AuthenticatedHttpClient httpClient;
    private ConceptService conceptService;
    private IdMappingsRepository identityMapper;


    private StringRegexUtils stringRegexUtils;

    public MedicationEventWorker(TRFeedProperties properties, AuthenticatedHttpClient httpClient, ConceptService conceptService, IdMappingsRepository identityMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.conceptService = conceptService;
        this.identityMapper = identityMapper;
        stringRegexUtils= new StringRegexUtils();
    }

    @Override
    public void process(Event event) {
        logger.info(format("Received medication sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        Medication medication = httpClient.get(properties.getMedicationUrl(event.getContent()), Medication.class);
        Concept drugConcept = mapConcept(medication.getCode());
        if (drugConcept == null) {
            String message = format("Can not identify concept for the medication %s", event.getContent());
            logger.error(message);
            throw new RuntimeException(message);
        }

        Concept drugForm = mapMedicationForm(medication.getProduct());
        String drugExternalId = identifyDrugLocalUuid(event.getContent());
        IdMapping idMap = identityMapper.findByExternalId(drugExternalId);

        Drug drug = (idMap != null) ? conceptService.getDrugByUuid(idMap.getInternalId()) : new Drug();

        drug.setName(medication.getName());
        drug.setConcept(drugConcept);
        drug.setDosageForm(drugForm);
        drug.setStrength(getExtensionValue(medication, "#strength"));
        drug.setRetired(getDrugRetiredStatus(medication));

        Drug savedDrug = conceptService.saveDrug(drug);
        if (idMap == null) {
            String baseUrl= stringRegexUtils.getBaseUrl(properties.medicationFeedUri());
            IdMapping medicationMap = new IdMapping(savedDrug.getUuid(), drugExternalId, "Medication", baseUrl+event.getContent());
            identityMapper.saveMapping(medicationMap);
        }
    }

    private Boolean getDrugRetiredStatus(Medication medication) {
        String retired = getExtensionValue(medication, "#retired");
        return StringUtils.isNotBlank(retired) ? Boolean.valueOf(retired) : false;
    }

    private String getExtensionValue(Medication medication, String suffix) {
        for (ResourceExtension resourceExtension : medication.getExtension()) {
            if (resourceExtension.getUrl().endsWith(suffix))
                return resourceExtension.getValueString();
        }
        return null;
    }

    private String identifyDrugLocalUuid(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    private Concept mapMedicationForm(MedicationProduct product) {
        if(product != null) {
            CodeableConcept form = product.getForm();
            return form != null ? mapConcept(form) : null;
        }
        return null;
    }

    private Concept mapConcept(CodeableConcept code) {
        List<Coding> codings = code.getCoding();
        Concept concept = null;
        for (Coding coding : codings) {
            if (isMRSConcept(coding)) {
                concept = identifyConcept(coding.getCode());
                break;
            }
        }
        return concept;
    }

    private Concept identifyConcept(String externalId) {
        IdMapping idMap = identityMapper.findByExternalId(externalId);
        if (idMap != null) {
            String internalUuid = idMap.getInternalId();
            return conceptService.getConceptByUuid(internalUuid);
        }
        return null;
    }

    private boolean isMRSConcept(Coding coding) {
        if (StringUtils.isNotBlank(coding.getSystem())) {
            return coding.getSystem().contains("/ws/rest/v1/tr/concepts/");
        }
        return false;
    }

    @Override
    public void cleanUp(Event event) {

    }
}
