package org.openmrs.module.htmlformentry19ext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.EncounterRole;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionActions;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class ProviderAndRoleFormSubmissionActionsExtenderTest {

    private ProviderAndRoleFormSubmissionActionsExtender extender = new ProviderAndRoleFormSubmissionActionsExtender();

    private FormEntrySession mockFormEntrySession;

    private FormSubmissionActions formSubmissionActions = new FormSubmissionActions();

    private Encounter encounter = new Encounter();

    private Map<String,List<Object>> otherActions;

    private Provider provider1 = new Provider();

    private Provider provider2 = new Provider();

    private EncounterRole role1 = new EncounterRole();

    private EncounterRole role2 = new EncounterRole();

    private User authenticatedUser = new User();

    @Before
    public void setup() {

        mockStatic(Context.class);
        when(Context.getAuthenticatedUser()).thenReturn(authenticatedUser);

        mockFormEntrySession = mock(FormEntrySession.class);

        when(mockFormEntrySession.getSubmissionActions()).thenReturn(formSubmissionActions);
        when(mockFormEntrySession.getEncounter()).thenReturn(encounter);

    }

    @Test
    public void applyActions_shouldCreateNewEncounterProviders() {

        EncounterProvider encounterProvider1 = new EncounterProvider();
        encounterProvider1.setEncounterRole(role1);
        encounterProvider1.setProvider(provider1);

        EncounterProvider encounterProvider2 = new EncounterProvider();
        encounterProvider2.setEncounterRole(role2);
        encounterProvider2.setProvider(provider2);

        formSubmissionActions.addOtherAction(HtmlFormEntryExtensions19Constants.ENSURE_ENCOUNTER_PROVIDER_EXISTS_ACTION,
                encounterProvider1);

        formSubmissionActions.addOtherAction(HtmlFormEntryExtensions19Constants.ENSURE_ENCOUNTER_PROVIDER_EXISTS_ACTION,
                encounterProvider2);

        extender.applyActions(mockFormEntrySession);

        Map<EncounterRole, Set<Provider>> providersByRole = mockFormEntrySession.getEncounter().getProvidersByRoles();

        assertThat(providersByRole.size(), is(2));
        assertThat(providersByRole.get(role1).size(), is(1));
        assertThat(providersByRole.get(role1).iterator().next(), is(provider1));
        assertThat(providersByRole.get(role2).size(), is(1));
        assertThat(providersByRole.get(role2).iterator().next(), is(provider2));
    }

}
