package org.openmrs.module.htmlformentry19ext.util;

import org.junit.Test;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HtmlFormEntry19ExtUtilsTest extends BaseModuleContextSensitiveTest {

    @Test
    public void getFullNameWithFamilyNameFirst_shouldReturnProperSimpleName() {
        PersonName name = new PersonName();
        name.setGivenName("Mark");
        name.setFamilyName("Goodrich");
        assertThat(HtmlFormEntryExtensions19Utils.getFullNameWithFamilyNameFirst(name), is("Goodrich, Mark"));
    }

    @Test
    public void getFullNameWithFamilyNameFirst_shouldReturnProperFullName() {
        PersonName name = new PersonName();
        name.setPrefix("Mr.");
        name.setGivenName("Mark");
        name.setMiddleName("Brutus");
        name.setFamilyNamePrefix("de");
        name.setFamilyName("Cameroon");
        name.setFamilyName2("Smith");
        name.setFamilyNameSuffix("Jr.");
        name.setDegree("Esq.");
        assertThat(HtmlFormEntryExtensions19Utils.getFullNameWithFamilyNameFirst(name), is("de Cameroon Smith Jr., Mr. Mark Brutus Esq."));
    }

    @Test
    public void getFullNameWithFamilyNameFirst_shouldNotFailIfProviderDoesNotHaveFamilyName() {
        PersonName name = new PersonName();
        name.setGivenName("Mark");
        assertThat(HtmlFormEntryExtensions19Utils.getFullNameWithFamilyNameFirst(name), is("Mark"));
    }

    @Test
    public void getFullNameWithFamilyNameFirst_shouldNotFailIfProviderHasNoName() {
        PersonName name = new PersonName();
        assertThat(HtmlFormEntryExtensions19Utils.getFullNameWithFamilyNameFirst(name), is(""));
    }
    
	@Test
	public void getFullNameWithFamilyNameFirst_shouldReturnDeletedIfPersonNameIsNull() {
		assertThat(HtmlFormEntryExtensions19Utils.getFullNameWithFamilyNameFirst(null), is("["
		        + Context.getMessageSourceService().getMessage("htmlformentry19ext.unknownProviderName") + "]"));
	}

}
