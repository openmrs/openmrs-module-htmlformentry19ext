package org.openmrs.module.htmlformentry19ext;

import org.openmrs.EncounterProvider;
import org.openmrs.EncounterRole;
import org.openmrs.Provider;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.extender.FormSubmissionActionsExtender;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProviderAndRoleFormSubmissionActionsExtender implements FormSubmissionActionsExtender {

    @Override
    public void applyActions(FormEntrySession session) {

        Map<EncounterRole, Set<Provider>> existingProvidersByRole = session.getEncounter().getProvidersByRoles();

        // simply add the new encounter providers if no existing providers
        if (existingProvidersByRole == null || existingProvidersByRole.isEmpty()) {
            addEncounterProviders(session, getEncounterProvidersFromForm(session));
        }

        // TODO: need to handle the cases where there are existing providers

    }


    private void addEncounterProviders(FormEntrySession session, List<Object> encounterProvidersToAdd) {
        if (encounterProvidersToAdd != null) {
            for (Object e : encounterProvidersToAdd) {
                EncounterProvider encounterProviderToAdd = (EncounterProvider) e;
                session.getEncounter().addProvider(encounterProviderToAdd.getEncounterRole(),
                        encounterProviderToAdd.getProvider());
            }
        }
    }

    private List<Object> getEncounterProvidersFromForm(FormEntrySession session) {
        return session.getSubmissionActions().getOtherActions()
                .get(HtmlFormEntryExtensions19Constants.ENSURE_ENCOUNTER_PROVIDER_EXISTS_ACTION);
    }


}
