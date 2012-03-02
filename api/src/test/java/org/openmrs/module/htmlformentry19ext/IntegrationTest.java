/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.htmlformentry19ext;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.RegressionTestHelper;
import org.openmrs.module.htmlformentry.TestUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * Integration test for the <encounterProviderAndRole/> tag 
 */
public class IntegrationTest extends BaseModuleContextSensitiveTest {

	@Before
	public void setup() throws Exception {
		executeDataSet("org/openmrs/module/htmlformentry19ext/include/IntegrationTests.xml");
		new HTMLFormEntryExtensions19Activator().started();
	}
	
	@Test
	public void testPlainTag() throws Exception {
		final Date date = new Date();
		new RegressionTestHelper() {
			
			@Override
			protected String getXmlDatasetPath() {
				return "org/openmrs/module/htmlformentry19ext/include/";
			}
			
			@Override
            public String getFormName() {
				return "plainEncounterProviderAndRoleTag";
			}
						
			@Override
			public String[] widgetLabels() {
				return new String[] { "Date:", "Location:", "ProviderAndRole:", "ProviderAndRole:!!1" };
			}
			
			@Override
			public void setupRequest(MockHttpServletRequest request, Map<String, String> widgets) {
				request.addParameter(widgets.get("Date:"), dateAsString(date));
				request.addParameter(widgets.get("Location:"), "2");
				request.addParameter(widgets.get("ProviderAndRole:"), "3"); // encounter role
				request.addParameter(widgets.get("ProviderAndRole:!!1"), "2"); // provider
			}
			
			@Override
			public void testResults(SubmissionResults results) {
				results.assertNoErrors();
				results.assertEncounterCreated();
				results.assertLocation(2);
				Map<EncounterRole, Set<Provider>> byRoles = results.getEncounterCreated().getProvidersByRoles();
				Assert.assertEquals(1, byRoles.size());
				EncounterRole encRole = byRoles.keySet().iterator().next();
				Assert.assertEquals(Integer.valueOf(3), encRole.getEncounterRoleId());
				Assert.assertEquals(Integer.valueOf(2), byRoles.get(encRole).iterator().next().getProviderId());
			}
		}.run();
	}
	
	@Test
	public void testDefaultValue() throws Exception {
		new RegressionTestHelper() {
			
			@Override
			protected String getXmlDatasetPath() {
				return "org/openmrs/module/htmlformentry19ext/include/";
			}
			
			@Override
            public String getFormName() {
				return "encounterProviderAndRoleTagWithDefault";
			}

			@Override
			public void testBlankFormHtml(String html) {
				Assert.assertTrue(html.contains("<option selected=\"true\" value=\"2\">"));
			};
		}.run();
	}
	
	@Test
	public void testTagSpecifyingEncounterRoleTwice() throws Exception {
		final Date date = new Date();
		new RegressionTestHelper() {
			
			@Override
			protected String getXmlDatasetPath() {
				return "org/openmrs/module/htmlformentry19ext/include/";
			}
			
			@Override
            public String getFormName() {
				return "specifyingEncounterRoleTwice";
			}
						
			@Override
			public String[] widgetLabels() {
				return new String[] { "Date:", "Location:", "Doctor:", "Nurse:" };
			}
			
			@Override
			public void setupRequest(MockHttpServletRequest request, Map<String, String> widgets) {
				request.addParameter(widgets.get("Date:"), dateAsString(date));
				request.addParameter(widgets.get("Location:"), "2");
				request.addParameter(widgets.get("Doctor:"), "2"); // Doctor Bob
				request.addParameter(widgets.get("Nurse:"), "1"); // Superuser
			}
					
			@Override
			public void testResults(SubmissionResults results) {
				results.assertNoErrors();
				results.assertEncounterCreated();
				results.assertLocation(2);
				Map<EncounterRole, Set<Provider>> byRoles = results.getEncounterCreated().getProvidersByRoles();
				Assert.assertEquals(2, byRoles.size());
				Set<Provider> doctors = byRoles.get(Context.getEncounterService().getEncounterRole(3));
				Set<Provider> nurses = byRoles.get(Context.getEncounterService().getEncounterRole(2));
				Assert.assertEquals(1, doctors.size());
				Assert.assertEquals(1, nurses.size());
				Assert.assertEquals(2, (int) doctors.iterator().next().getProviderId());
				Assert.assertEquals(1, (int) nurses.iterator().next().getProviderId());
			}
			
			@Override
			public boolean doViewEncounter() {
				return true;
			}
			
			@Override
			public void testViewingEncounter(Encounter encounter, String html) {
				TestUtil.assertFuzzyEquals("Date:" + Context.getDateFormat().format(date) + " Location:Xanadu Doctor:Doctor Bob, M.D. Nurse:Super User", html);
			}
			
		}.run();
	}
	
}
