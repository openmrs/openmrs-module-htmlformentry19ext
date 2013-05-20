package org.openmrs.module.htmlformentry19ext.util;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.providermanagement.ProviderRole;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HtmlFormEntryExtensions19UtilsTest extends BaseModuleContextSensitiveTest {

    @Before
    public void setup() throws Exception {
        executeDataSet("org/openmrs/module/htmlformentry19ext/include/providerRoles-dataset.xml");
    }

    @Test
    public void getProviderRole_shouldGetProviderRoleById() {
        Object providerRole = HtmlFormEntryExtensions19Utils.getProviderRole("1002");
        assertThat(((ProviderRole) providerRole).getName(), is("Binome supervisor"));
    }

    @Test
    public void getProviderRole_shouldGetProviderRoleByUuid() {
        Object providerRole = HtmlFormEntryExtensions19Utils.getProviderRole("ea7f523f-27ce-4bb2-86d6-6d1d05312bd5");
        assertThat(((ProviderRole) providerRole).getName(), is("Binome supervisor"));
    }

    @Test
    public void getProviderRole_shouldReturnNullIfBogusId() {
        Object providerRole = HtmlFormEntryExtensions19Utils.getProviderRole("some bogus text");
        assertNull(providerRole);
    }

    @Test
    public void getProviderRole_shouldReturnNullIfBlank() {
        Object providerRole = HtmlFormEntryExtensions19Utils.getProviderRole("");
        assertNull(providerRole);
    }

    @Test
    public void getProviders_shouldReturnProvidersForSingleRole() {
        ProviderRole providerRole = Context.getService(ProviderManagementService.class).getProviderRole(1002);
        List<Provider> providers = HtmlFormEntryExtensions19Utils.getProviders(Collections.singletonList((Object) providerRole));
        assertThat(providers.size(), is(1));
        assertThat(providers.get(0).getId(), is(1006));
    }

    @Test
    public void getProviders_shouldReturnProvidersForMultipleRole() {

        List<Object> providerRoles = new ArrayList<Object>();
        providerRoles.add((Object) Context.getService(ProviderManagementService.class).getProviderRole(1001));
        providerRoles.add((Object) Context.getService(ProviderManagementService.class).getProviderRole(1002));

        List<Provider> providers = HtmlFormEntryExtensions19Utils.getProviders(providerRoles);
        assertThat(providers.size(), is(4));

        List<Integer> resultProviderIds = Arrays.asList(1003,1004,1005, 1006);

        for (Provider provider : providers) {
            assertTrue(resultProviderIds.contains(provider.getId()));
        }
    }

    @Test
    public void getProviders_shouldReturnEmptyListIfNoMatches() {
        ProviderRole providerRole = Context.getService(ProviderManagementService.class).getProviderRole(1004);
        List<Provider> providers = HtmlFormEntryExtensions19Utils.getProviders(Collections.singletonList((Object) providerRole));
        assertThat(providers.size(), is(0));
    }

    @Test
    public void getProviders_shouldReturnEmptyListIfPassedNull() {
        List<Provider> providers = HtmlFormEntryExtensions19Utils.getProviders(null);
        assertThat(providers.size(), is(0));
    }

    @Test
    public void getProviders_shouldReturnEmptyListIfPassedEmptyList() {
        List<Provider> providers = HtmlFormEntryExtensions19Utils.getProviders(new ArrayList<Object>());
        assertThat(providers.size(), is(0));
    }

}
